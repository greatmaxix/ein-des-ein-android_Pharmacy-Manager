package com.pharmacy.manager.components.devTools

import com.pharmacy.manager.R
import com.pharmacy.manager.core.base.mvvm.BaseMVVMFragment
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class DevToolsFragment(private val vm: DevToolsViewModel) :
    BaseMVVMFragment(R.layout.fragment_dev_tools)