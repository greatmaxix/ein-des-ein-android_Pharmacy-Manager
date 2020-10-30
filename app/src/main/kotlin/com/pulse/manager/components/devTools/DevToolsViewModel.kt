package com.pulse.manager.components.devTools

import com.pulse.manager.components.devTools.repository.DevToolsRepository
import com.pulse.manager.core.base.mvvm.BaseViewModel
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class DevToolsViewModel(private val repository: DevToolsRepository) : BaseViewModel()