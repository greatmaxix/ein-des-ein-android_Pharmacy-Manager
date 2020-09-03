package com.pharmacy.manager.core.base.mvvm

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.pharmacy.manager.core.base.fragment.dialog.BaseBottomSheetDialogFragment

abstract class BaseMVVMFragmentDialogBottomSheet(@LayoutRes layoutResourceId: Int) :
    BaseBottomSheetDialogFragment(layoutResourceId) {
    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBindLiveData()
    }

    /**
     * Here we may bind our observers to LiveData if some.
     * This method will be executed after parent [onCreateView] method
     */
    protected open fun onBindLiveData() {
        //Optional
    }

    protected fun <T, LD : LiveData<T>> observeSingle(liveData: LD, onChanged: (T?) -> Unit) {
        liveData.observe(viewLifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                onChanged.invoke(t)
                liveData.removeObserver(this)
            }
        })
    }

    protected fun <T, LD : LiveData<T>> observeNullable(liveData: LD, onChanged: T?.() -> Unit) {
        liveData.observe(this, { value ->
            onChanged(value)
        })
    }

    protected fun <T, LD : LiveData<T>> observe(liveData: LD, onChanged: T.() -> Unit) {
        liveData.observe(this, { value ->
            value?.let(onChanged)
        })
    }
}