package com.pharmacy.manager.core.base.mvvm

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.pharmacy.manager.core.base.fragment.BaseFragment
import com.pharmacy.manager.core.dsl.ObserveGeneral
import com.pharmacy.manager.core.network.Resource.*
import kotlinx.coroutines.flow.Flow

abstract class BaseMVVMFragment(@LayoutRes layoutResourceId: Int) : BaseFragment(layoutResourceId) {

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

    protected fun <T> observeRestResult(block: ObserveGeneral<T>.() -> Unit) {
        ObserveGeneral<T>().apply(block).apply {
            observe(liveData) {
                when (this) {
                    is Success<T> -> {
                        progressCallback?.setInProgress(false)
                        onEmmit.invoke(data)
                    }
                    is Progress -> {
                        onProgress?.invoke(isLoading) ?: progressCallback?.setInProgress(isLoading)
                    }
                    is Error -> {
                        progressCallback?.setInProgress(false)
                        onError?.invoke(exception) ?: messageCallback?.showError(exception.resId)
                    }
                }
            }
        }
    }

    protected fun <T, F : Flow<T>> observeFlow(flow: F, onChanged: T.() -> Unit) {
        observe(flow.asLiveData(viewLifecycleOwner.lifecycleScope.coroutineContext), onChanged)
    }
}