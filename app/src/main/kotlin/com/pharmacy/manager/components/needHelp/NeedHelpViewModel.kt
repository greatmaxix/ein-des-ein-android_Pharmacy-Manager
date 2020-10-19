package com.pharmacy.manager.components.needHelp

import com.pharmacy.manager.components.needHelp.repository.NeedHelpRepository
import com.pharmacy.manager.core.base.mvvm.BaseViewModel
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class NeedHelpViewModel(private val repository: NeedHelpRepository) : BaseViewModel()