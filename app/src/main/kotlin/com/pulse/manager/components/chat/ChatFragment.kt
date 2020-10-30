package com.pulse.manager.components.chat

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.ExperimentalPagingApi
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.anyDenied
import com.fondesa.kpermissions.anyPermanentlyDenied
import com.fondesa.kpermissions.anyShouldShowRationale
import com.fondesa.kpermissions.extension.addListener
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.pulse.manager.BuildConfig
import com.pulse.manager.R
import com.pulse.manager.components.chat.ChatFragmentDirections.Companion.fromChatToSendImageBottomSheet
import com.pulse.manager.components.chat.ChatFragmentDirections.Companion.globalToHome
import com.pulse.manager.components.chat.adapter.ChatMessageAdapter
import com.pulse.manager.components.chat.adapter.ProductAttachSearchAdapter
import com.pulse.manager.components.chat.dialog.SendBottomSheetDialogFragment
import com.pulse.manager.components.chat.dialog.SendBottomSheetDialogFragment.Companion.RESULT_BUTTON_EXTRA_KEY
import com.pulse.manager.components.chat.dialog.SendBottomSheetDialogFragment.Companion.SEND_PHOTO_KEY
import com.pulse.manager.components.chatList.model.chat.ChatItem
import com.pulse.manager.core.base.mvvm.BaseMVVMFragment
import com.pulse.manager.core.extensions.*
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.core.parameter.parametersOf

@KoinApiExtension
@FlowPreview
class ChatFragment : BaseMVVMFragment(R.layout.fragment_chat) {

    private val args by navArgs<ChatFragmentArgs>()
    private val vm: ChatViewModel by viewModel(parameters = { parametersOf(args.chat) })
    private val uri by lazy { FileProvider.getUriForFile(requireContext(), "${BuildConfig.APPLICATION_ID}.fileprovider", vm.tempPhotoFile) }
    private lateinit var choosePhotoLauncher: ActivityResultLauncher<Intent>
    private lateinit var takePhotoLauncher: ActivityResultLauncher<Uri>

    private val chatAdapter by lazy { ChatMessageAdapter() }
    private val productAdapter by lazy {
        ProductAttachSearchAdapter {
            observeResult(vm.sendProduct(it))
            searchViewChatList.setText("")
            hideProducts()
        }
    }
    private var scrollerJob: Job? = null

    @FlowPreview
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton()
        initMenu(R.menu.close_request) {
            if (it.itemId == R.id.closeRequest) {
                observeResult(vm.requestCloseChat())
            }
            true
        }
        initChatList()
        initProductList()

        choosePhotoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) onActivityResult(it)
        }
        takePhotoLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it) sendPhotoMessage(uri)
        }

        tilMessageChat.editText?.doAfterTextChanged {
            ivButtonSendChat.animateVisibleOrGoneIfNot(!it.isNullOrBlank())
        }
        tilMessageChat.editText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendMessage()
            }
            false
        }
        ivButtonSendChat.setDebounceOnClickListener {
            sendMessage()
        }
        ivFileAttachment.setDebounceOnClickListener {
            navController.navigate(fromChatToSendImageBottomSheet())
        }
        ivProductAttachment.setDebounceOnClickListener {
            if (llProductContainer.isVisible) {
                hideProducts()
            } else {
                llProductContainer.animateVisibleIfNot()
                ivProductAttachment.isSelected = true
                searchViewChatList.requestFocus()
            }
        }
        llMessageFieldChat.setTopRoundCornerBackground()

        attachBackPressCallback {
            if (llProductContainer.isVisible) {
                hideProducts()
            } else {
                navController.popBackStack()
            }
        }
        searchViewChatList.setSearchListener { text ->
            vm.doSearch(text.toString())
        }
        setFragmentResultListener(SEND_PHOTO_KEY) { _, bundle ->
            when (bundle.getString(RESULT_BUTTON_EXTRA_KEY)) {
                SendBottomSheetDialogFragment.Button.GALLERY.name -> requestPickPhoto()
                SendBottomSheetDialogFragment.Button.CAMERA.name -> requestTakePhoto()
            }
        }
        llMessageFieldChat.visible()
    }

    private fun hideProducts() {
        llProductContainer.animateGoneIfNot()
        ivProductAttachment.isSelected = false
        searchViewChatList.clearFocus()
    }

    private fun initProductList() {
        rvProductChat.adapter = productAdapter
        rvProductChat.setHasFixedSize(true)
    }

    private fun onActivityResult(result: ActivityResult) {
        val data = result.data?.data
        if (result.resultCode == Activity.RESULT_OK && data != null) {
            sendPhotoMessage(data)
        }
    }

    private fun sendPhotoMessage(uri: Uri) {
        observeResult(vm.sendPhoto(uri))
    }

    @FlowPreview
    private fun requestPickPhoto() {
        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        choosePhotoLauncher.launch(pickIntent)
    }

    private fun requestTakePhoto() {
        val isDeviceSupportCamera: Boolean = activity?.packageManager?.hasSystemFeature(
            PackageManager.FEATURE_CAMERA_ANY
        ) ?: false
        if (isDeviceSupportCamera) {
            val request = permissionsBuilder(Manifest.permission.CAMERA).build()
            request.addListener { result ->
                when {
                    result.anyPermanentlyDenied() -> openSettings()
                    result.anyShouldShowRationale() -> {
                        showAlertRes(getString(R.string.cameraPermissionRationaleMessageChat)) {
                            cancelable = false
                            positive = R.string.common_okButton
                            positiveAction = { request.send() }
                            negative = R.string.common_closeButton
                        }
                    }
                    result.anyDenied() -> messageCallback?.showError(getString(R.string.cameraPermissionDenied))
                    result.allGranted() -> takePhotoLauncher.launch(uri)
                }
            }
            request.send()
        } else {
            messageCallback?.showError(getString(R.string.cameraPermissionNoCameraOnDevice))
        }
    }

    private fun openSettings() {
        showAlertRes(getString(R.string.cameraPermissionPermanentlyDenied)) {
            cancelable = false
            positive = R.string.permissionDialogSettingsButton
            positiveAction = {
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                intent.data = Uri.fromParts("package", requireActivity().packageName, null)
                requireContext().startActivity(intent)
            }
            negative = R.string.permissionDialogCancel
        }
    }

    private fun sendMessage() {
        val message = tilMessageChat.editText?.text?.toString()?.trim().orEmpty()
        tilMessageChat.editText?.text = null
        observeResult(vm.sendMessage(message))
    }

    @ExperimentalPagingApi
    override fun onBindLiveData() {
        super.onBindLiveData()
        observe(vm.chatLiveData) {
            this?.let {
                toolbar?.title = customer.name
                toolbar?.menu?.findItem(R.id.closeRequest)?.isVisible = isAbleToWrite && it.status != ChatItem.STATUS_OPENED
                llMessageFieldChat.isVisible = isAbleToWrite

                if (args.chat.isAbleToWrite) {
                    if (isAutomaticClosed) showChatEndDialog()
                    else if (isClosed) showChatEndDialog()
                }
            }
        }
        observe(vm.chatMessagesLiveData) { chatAdapter.submitData(lifecycle, this) }
        observe(vm.lastMessageLiveData) { scrollToLastMessage() }
        observe(vm.pagedSearchLiveData) { productAdapter.submitData(lifecycle, this) }
    }

    private fun showChatEndDialog() {
        showAlertRes(getString(R.string.chatEndedMessage)) {
            cancelable = false
            title = R.string.chatEndedTitle
            positive = R.string.common_okButton
            positiveAction = { navController.navigate(globalToHome()) }
        }
    }

    private fun initChatList() = with(rvMessagesChat) {
        adapter = chatAdapter
    }

    private fun scrollToLastMessage() {
        scrollerJob?.cancel()
        if (chatAdapter.itemCount != 0) {
            scrollerJob = viewLifecycleOwner.lifecycleScope.launch {
                delay(500)
                rvMessagesChat.smoothScrollToPosition(0)
            }
        }
    }
}