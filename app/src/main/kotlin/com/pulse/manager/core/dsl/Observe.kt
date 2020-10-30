package com.pulse.manager.core.dsl

import com.pulse.manager.data.GeneralException

open class ObserveGeneral<DATA>(
    var onEmmit: (DATA.() -> Unit) = {},
    var onError: ((GeneralException) -> Unit)? = null,
    var onProgress: ((Boolean) -> Unit)? = null
)