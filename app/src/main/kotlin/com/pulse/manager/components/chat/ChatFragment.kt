package com.pulse.manager.components.chat

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
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
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.manager.BuildConfig
import com.pulse.manager.R
import com.pulse.manager.components.chat.ChatFragmentDirections.Companion.fromChatToSendImageBottomSheet
import com.pulse.manager.components.chat.ChatFragmentDirections.Companion.globalToHome
import com.pulse.manager.components.chat.adapter.ChatMessageAdapter
import com.pulse.manager.components.chat.adapter.ProductAttachSearchAdapter
import com.pulse.manager.components.chat.dialog.SendBottomSheetDialogFragment
import com.pulse.manager.components.chat.dialog.SendBottomSheetDialogFragment.Companion.RESULT_BUTTON_EXTRA_KEY
import com.pulse.manager.components.chat.dialog.SendBottomSheetDialogFragment.Companion.SEND_PHOTO_KEY
import com.pulse.manager.components.chat_list.model.chat.ChatItem
import com.pulse.manager.core.base.fragment.BaseToolbarFragment
import com.pulse.manager.core.extensions.*
import com.pulse.manager.databinding.FragmentChatBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.core.parameter.parametersOf

@ExperimentalCoroutinesApi
@KoinApiExtension
@FlowPreview
class ChatFragment : BaseToolbarFragment<ChatViewModel>(R.layout.fragment_chat, ChatViewModel::class, R.menu.close_request) {

    private val args by navArgs<ChatFragmentArgs>()
    private val binding by viewBinding(FragmentChatBinding::bind)
    override val viewModel: ChatViewModel by viewModel(parameters = { parametersOf(args.chat) })
    private val uri by lazy { FileProvider.getUriForFile(requireContext(), "${BuildConfig.APPLICATION_ID}.fileprovider", viewModel.tempPhotoFile) }
    private lateinit var choosePhotoLauncher: ActivityResultLauncher<Intent>
    private lateinit var takePhotoLauncher: ActivityResultLauncher<Uri>
    private val chatAdapter by lazy { ChatMessageAdapter() }
    private val productAdapter by lazy {
        ProductAttachSearchAdapter {
            viewModel.sendProduct(it)
            binding.viewSearch.setText("")
            hideProducts()
        }
    }
    private var scrollerJob: Job? = null

    override fun initUI() = with(binding) {
        showBackButton()
        initChatList()
        initProductList()

        choosePhotoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { if (it.resultCode == Activity.RESULT_OK) onActivityResult(it) }
        takePhotoLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { if (it) sendPhotoMessage(uri) }
        tilMessage.editText?.doAfterTextChanged { ivButtonSend.animateVisibleOrGoneIfNot(!it.isNullOrBlank()) }
        tilMessage.editText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) sendMessage()
            false
        }
        ivButtonSend.setDebounceOnClickListener { sendMessage() }
        ivFileAttachment.setDebounceOnClickListener { navController.navigate(fromChatToSendImageBottomSheet()) }
        ivProductAttachment.setDebounceOnClickListener {
            if (llProductContainer.isVisible) {
                hideProducts()
            } else {
                llProductContainer.animateVisibleIfNot()
                ivProductAttachment.isSelected = true
                viewSearch.requestFocus()
            }
        }
        llMessageField.setTopRoundCornerBackground()

        attachBackPressCallback {
            if (llProductContainer.isVisible) {
                hideProducts()
            } else {
                navController.popBackStack()
            }
        }
        viewSearch.setSearchListener { text ->
            viewModel.doSearch(text.toString())
        }
        setFragmentResultListener(SEND_PHOTO_KEY) { _, bundle ->
            when (bundle.getString(RESULT_BUTTON_EXTRA_KEY)) {
                SendBottomSheetDialogFragment.Button.GALLERY.name -> requestPickPhoto()
                SendBottomSheetDialogFragment.Button.CAMERA.name -> requestTakePhoto()
            }
        }
        llMessageField.visible()
    }

    private fun hideProducts() = with(binding) {
        llProductContainer.animateGoneIfNot()
        ivProductAttachment.isSelected = false
        viewSearch.clearFocus()
    }

    private fun initProductList() = with(binding.rvProduct) {
        adapter = productAdapter
        setHasFixedSize(true)
    }

    private fun onActivityResult(result: ActivityResult) {
        val data = result.data?.data
        if (result.resultCode == Activity.RESULT_OK && data != null) {
            sendPhotoMessage(data)
        }
    }

    private fun sendPhotoMessage(uri: Uri) = viewModel.sendPhoto(uri)

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
            requestPermissions(
                firstPermission = Manifest.permission.CAMERA,
                openSettingsMessage = R.string.cameraPermissionPermanentlyDenied,
                rationaleMessage = R.string.cameraPermissionRationaleMessage,
                deniedMessage = R.string.cameraPermissionDenied
            ) { takePhotoLauncher.launch(uri) }
        } else {
            uiHelper.showMessage(getString(R.string.cameraPermissionNoCameraOnDevice))
        }
    }

    private fun sendMessage() {
        val message = binding.tilMessage.editText?.text?.toString()?.trim().orEmpty()
        binding.tilMessage.editText?.text = null
        viewModel.sendMessage(message)
    }

    override fun onBindEvents() = with(lifecycleScope) {
        observe(menuItemsFlow) {
            if (it.itemId == R.id.closeRequest) {
                viewModel.requestCloseChat()
            }
        }
    }

    @ExperimentalPagingApi
    override fun onBindStates() = with(lifecycleScope) {
        observe(viewModel.chatMessagesFlow) { chatAdapter.submitData(lifecycle, it) }
        observe(viewModel.lastMessageFlow) { scrollToLastMessage() }
        observe(viewModel.pagedSearchFlow) { productAdapter.submitData(lifecycle, it) }
        observe(viewModel.chatFlow) {
            it?.let {
                with(binding) {
                    toolbar.toolbar.title = it.customer.name
                    toolbar.toolbar.menu?.findItem(R.id.closeRequest)?.isVisible = it.isAbleToWrite && it.status != ChatItem.STATUS_OPENED
                    llMessageField.isVisible = it.isAbleToWrite
                    if (!it.isAbleToWrite) hideKeyboard()
                    if (args.chat.isAbleToWrite) {
                        if (it.isAutomaticClosed) showChatEndDialog()
                        else if (it.isClosed) showChatEndDialog()
                    }
                }
            }
        }
    }

    private fun showChatEndDialog() {
        hideKeyboard()
        showAlertRes(getString(R.string.chatEndedMessage)) {
            cancelable = false
            title = R.string.chatEndedTitle
            positive = R.string.common_okButton
            positiveAction = { navController.navigate(globalToHome()) }
        }
    }

    private fun initChatList() = with(binding.rvMessages) {
        adapter = chatAdapter
    }

    private fun scrollToLastMessage() {
        scrollerJob?.cancel()
        if (chatAdapter.itemCount != 0) {
            scrollerJob = viewLifecycleOwner.lifecycleScope.launch {
                binding.rvMessages.smoothScrollToPosition(0)
            }
        }
    }
}