package com.pulse.manager.core.extensions

import com.pulse.manager.core.network.Resource
import com.pulse.manager.core.network.Result
import kotlinx.coroutines.flow.*

fun <LD, RD> Flow<Result<RD>>.flatMapRemoteLocal(
    localData: () -> Flow<LD>,
    predicateIfNeedSave: (suspend (RD) -> Unit)? = null,
    predicateIsNeedRemote: (suspend (LD?) -> Boolean)? = null
) = flow {

    emit(Resource.Progress())

    if (predicateIsNeedRemote != null && predicateIsNeedRemote(localData().first())) {
        collect { result ->
            when (result) {
                is Result.ResultSuccess -> {
                    result.body?.let { predicateIfNeedSave?.invoke(it) }
                    emit(Resource.Progress(false))
                    emitAll(localData().map { Resource.Success(it) })
                }
                is Result.ResultError -> {
                    emit(Resource.Progress(false))
                    emitAll(localData().map { Resource.Error(result.exception) })
                }
            }
        }
    } else {
        emit(Resource.Progress(false))
        emitAll(localData().map { Resource.Success(it) })
    }
}