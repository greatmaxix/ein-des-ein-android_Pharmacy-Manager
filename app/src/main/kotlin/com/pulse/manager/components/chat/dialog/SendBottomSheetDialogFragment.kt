package com.pulse.manager.components.chat.dialog

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.manager.R
import com.pulse.manager.core.base.fragment.dialog.BaseBottomSheetDialogFragment
import com.pulse.manager.core.extensions.setDebounceOnClickListener
import com.pulse.manager.databinding.DialogSendPhotoBottomSheetBinding

class SendBottomSheetDialogFragment : BaseBottomSheetDialogFragment(R.layout.dialog_send_photo_bottom_sheet) {

    private val binding by viewBinding(DialogSendPhotoBottomSheetBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        mtvGallery.setDebounceOnClickListener {
            setFragmentResult(SEND_PHOTO_KEY, bundleOf(RESULT_BUTTON_EXTRA_KEY to Button.GALLERY.name))
            dismiss()
        }
        mtvCamera.setDebounceOnClickListener {
            setFragmentResult(SEND_PHOTO_KEY, bundleOf(RESULT_BUTTON_EXTRA_KEY to Button.CAMERA.name))
            dismiss()
        }
    }

    enum class Button {
        GALLERY, CAMERA
    }

    companion object {

        const val SEND_PHOTO_KEY = "SEND_PHOTO_KEY"
        const val RESULT_BUTTON_EXTRA_KEY = "RESULT_BUTTON_EXTRA_KEY"
    }
}