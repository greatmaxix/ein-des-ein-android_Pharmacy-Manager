package com.pulse.manager.core.base.mvvm

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModel
import com.pulse.manager.core.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.getViewModel
import kotlin.reflect.KClass

abstract class BaseMVVMActivity<out VM : ViewModel>(@LayoutRes layoutResourceId: Int, viewModelClass: KClass<VM>) : BaseActivity(layoutResourceId), IBind {

    protected val viewModel: VM by lazy { getViewModel(clazz = viewModelClass) }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBindEvents()
        onBindStates()
    }

    /**
     * Here we may bind our observers for events as Navigation, Show dialog, Toast ect.
     * This method will be executed after parent [onCreate] method
     */
    override fun onBindEvents() {
        //Optional
    }

    /**
     * Here we may bind our observers for state changes as updating view ect.
     * This method will be executed after parent [onCreate] method
     */
    override fun onBindStates() {
        //Optional
    }
}