package com.pulse.manager.components.needHelp

import com.pulse.manager.components.needHelp.repository.NeedHelpRepository
import com.pulse.manager.core.base.mvvm.BaseViewModel
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class NeedHelpViewModel(private val repository: NeedHelpRepository) : BaseViewModel()