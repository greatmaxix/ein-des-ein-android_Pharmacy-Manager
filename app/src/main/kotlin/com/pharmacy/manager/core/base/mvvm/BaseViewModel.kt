package com.pharmacy.manager.core.base.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.pharmacy.manager.core.general.SingleLiveEvent
import com.pharmacy.manager.core.network.Resource.*
import com.pharmacy.manager.data.GeneralErrorHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
abstract class BaseViewModel : ViewModel() {

    protected val errorHandler by lazy { GeneralErrorHandler() }

    protected fun <R> requestSingleLiveData(
        needLoading: Boolean = true,
        dispatcher: CoroutineDispatcher = IO,
        request: suspend () -> R
    ) = SingleLiveEvent<R>().switchMap { requestLiveData(needLoading, dispatcher, request) }

    protected fun <R> requestLiveData(
        needLoading: Boolean = true,
        dispatcher: CoroutineDispatcher = IO,
        request: suspend () -> R
    ) = liveData(viewModelScope.coroutineContext + dispatcher) {
        if (needLoading) {
            emit(Progress(true))
        }
        try {
            emit(Success(request.invoke()))
        } catch (e: Exception) {
            emit(Error(errorHandler.checkThrowable(e)))
        }
    }

    fun launchIO(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(IO, block = block)
}