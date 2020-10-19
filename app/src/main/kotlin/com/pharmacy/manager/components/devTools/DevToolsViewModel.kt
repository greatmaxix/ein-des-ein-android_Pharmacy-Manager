package com.pharmacy.manager.components.devTools

import com.pharmacy.manager.components.devTools.repository.DevToolsRepository
import com.pharmacy.manager.core.base.mvvm.BaseViewModel
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class DevToolsViewModel(private val repository: DevToolsRepository) : BaseViewModel()