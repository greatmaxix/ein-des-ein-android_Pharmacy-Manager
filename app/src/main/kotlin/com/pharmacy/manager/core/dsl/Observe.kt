package com.pharmacy.manager.core.dsl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pharmacy.manager.core.network.Resource
import com.pharmacy.manager.data.GeneralException

open class ObserveGeneral<DATA>(
    var onEmmit: (DATA.() -> Unit) = {},
    var onError: ((GeneralException) -> Unit)? = null,
    var liveData: LiveData<Resource<DATA>> = MutableLiveData(),
    var onProgress: (() -> Unit)? = null
)