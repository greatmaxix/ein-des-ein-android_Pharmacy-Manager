package com.pharmacy.manager.components.chat

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.onimur.handlepathoz.HandlePathOz
import br.com.onimur.handlepathoz.HandlePathOzListener
import br.com.onimur.handlepathoz.model.PathOz
import br.com.onimur.handlepathoz.utils.extension.getListUri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.anyDenied
import com.fondesa.kpermissions.anyPermanentlyDenied
import com.fondesa.kpermissions.anyShouldShowRationale
import com.fondesa.kpermissions.extension.addListener
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.pharmacy.manager.BuildConfig
import com.pharmacy.manager.R
import com.pharmacy.manager.components.chat.ChatFragmentDirections.Companion.fromChatToSendImageBottomSheet
import com.pharmacy.manager.components.chat.adapter.ChatMessageAdapter
import com.pharmacy.manager.components.chat.adapter.ProductAdapter
import com.pharmacy.manager.components.chat.dialog.SendBottomSheetDialogFragment
import com.pharmacy.manager.components.chat.dialog.SendBottomSheetDialogFragment.Companion.RESULT_BUTTON_EXTRA_KEY
import com.pharmacy.manager.components.chat.dialog.SendBottomSheetDialogFragment.Companion.SEND_PHOTO_KEY
import com.pharmacy.manager.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.manager.core.extensions.*
import com.pharmacy.manager.data.DummyData
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ChatFragment : BaseMVVMFragment(R.layout.fragment_chat) {

    private val uri by lazy { FileProvider.getUriForFile(requireContext(), "${BuildConfig.APPLICATION_ID}.fileprovider", vm.tempPhotoFile) }
    private val vm: ChatViewModel by viewModel(parameters = { parametersOf(ChatFragmentArgs.fromBundle(requireArguments())) })

    @FlowPreview
    private val choosePhotoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { handlePathOz.getListRealPath(it.data.getListUri()) }
    private val takePhotoLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { vm.sendPhotos(listOf(uri)) }
    private val handlePathOz by lazy { HandlePathOz(requireContext(), listener) }
    private val listener = object : HandlePathOzListener.MultipleUri {
        override fun onRequestHandlePathOz(listPathOz: List<PathOz>, tr: Throwable?) {
            if (tr != null) {
                messageCallback?.showError(getString(R.string.sendPhotoError))
            } else {
                vm.sendPhotos(listPathOz.map { uri -> Uri.parse(uri.path) })
            }
        }
    }

    private val chatAdapter by lazy { ChatMessageAdapter() }
    private val productAdapter by lazy {
        ProductAdapter {
            vm.sendProduct(it)
            searchViewChatList.setText("")
            hideProducts()
        }
    }

    @FlowPreview
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton()
        initMenu(R.menu.profile) {
            if (it.itemId == R.id.profile) {
                // TODO open profile maybe
            }
            true
        }
        initChatList()
        initProductList()

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
            llProductContainer.animateVisibleIfNot()
            ivProductAttachment.isSelected = true
        }
        llMessageFieldChat.setTopRoundCornerBackground()

        attachBackPressCallback {
            if (llProductContainer.isVisible) {
                hideProducts()
            } else {
                navController.popBackStack()
            }
        }

        setFragmentResultListener(SEND_PHOTO_KEY) { _, bundle ->
            when (bundle.getString(RESULT_BUTTON_EXTRA_KEY)) {
                SendBottomSheetDialogFragment.Button.GALLERY.name -> requestPickPhoto()
                SendBottomSheetDialogFragment.Button.CAMERA.name -> requestTakePhoto()
            }
        }
    }

    private fun hideProducts() {
        llProductContainer.animateGoneIfNot()
        ivProductAttachment.isSelected = false
    }

    private fun initProductList() {
        rvProductChat.adapter = productAdapter
        rvProductChat.layoutManager = GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
        rvProductChat.setHasFixedSize(true)

        productAdapter.notifyDataSet(DummyData.products)

        searchViewChatList.setSearchListener { text ->
            viewLifecycleOwner.lifecycleScope.launch {
                productAdapter.filter { it.rusName.contains(text, true).falseIfNull() || it.description.contains(text, true).falseIfNull() }
            }
        }
    }

    @FlowPreview
    private fun requestPickPhoto() {
        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
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
        vm.sendMessage(message)
    }

    override fun onBindLiveData() {
        super.onBindLiveData()

        observe(vm.directionLiveData, navController::navigate)
        observe(vm.errorLiveData) { messageCallback?.showError(this) }
        observe(vm.progressLiveData) { progressCallback?.setInProgress(this) }

        llMessageFieldChat.visible()

        observe(vm.chatLiveData) {
            toolbar?.title = name
            Glide.with(requireContext())
                .asBitmap()
                .load(avatar)
                .placeholder(R.drawable.ic_avatar_placeholder)
                .apply(RequestOptions.circleCropTransform())
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        toolbar?.menu?.findItem(R.id.profile)?.icon = BitmapDrawable(resources, resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // no op
                    }
                })
        }
        observe(vm.chatMessagesLiveData) {
            chatAdapter.setList(this)
            rvMessagesChat.postDelayed({
                rvMessagesChat.smoothScrollToPosition(0)
            }, 100)
        }
    }

    private fun initChatList() {
        rvMessagesChat.adapter = chatAdapter
        rvMessagesChat.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, true)
        rvMessagesChat.setHasFixedSize(true)
    }
}