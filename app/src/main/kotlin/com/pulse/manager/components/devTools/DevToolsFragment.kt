package com.pulse.manager.components.devTools

import com.pulse.manager.R
import com.pulse.manager.core.base.mvvm.BaseMVVMFragment
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class DevToolsFragment(private val vm: DevToolsViewModel) :
    BaseMVVMFragment(R.layout.fragment_dev_tools)