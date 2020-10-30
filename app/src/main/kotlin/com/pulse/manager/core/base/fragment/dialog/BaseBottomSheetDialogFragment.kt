package com.pulse.manager.core.base.fragment.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pulse.manager.core.extensions.getFragmentTag
import com.pulse.manager.core.general.behavior.IBehavior

abstract class BaseBottomSheetDialogFragment(@LayoutRes private val layoutResourceId: Int) :
    BottomSheetDialogFragment() {

    private val behaviors = mutableListOf<IBehavior?>()

    protected val bottomSheetBehavior by lazy { (dialog as BottomSheetDialog).behavior }

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        super.onCreateDialog(savedInstanceState) as BottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(layoutResourceId, container, false)

    @CallSuper
    override fun onDestroy() {
        behaviors.forEach { it?.detach() }
        behaviors.clear()
        super.onDestroy()
    }

    @CallSuper
    open fun show(manager: FragmentManager) {
        val tag = getFragmentTag()
        if (manager.findFragmentByTag(tag) == null) {
            super.show(manager, tag)
        }
    }

    protected fun <T : IBehavior> attachBehavior(behavior: T) = behavior.also { behaviors.add(it) }

    protected fun setState(@BottomSheetBehavior.State state: Int) {
        bottomSheetBehavior.state = state
    }
}