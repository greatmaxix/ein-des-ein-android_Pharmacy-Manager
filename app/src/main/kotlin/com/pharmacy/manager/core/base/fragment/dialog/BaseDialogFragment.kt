package com.pharmacy.manager.core.base.fragment.dialog

import androidx.annotation.CallSuper
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.pharmacy.manager.core.extensions.getFragmentTag

abstract class BaseDialogFragment : DialogFragment() {
    @CallSuper
    open fun show(manager: FragmentManager) {
        val tag = getFragmentTag()
        if (manager.findFragmentByTag(tag) == null) {
            super.show(manager, tag)
        }
    }
}