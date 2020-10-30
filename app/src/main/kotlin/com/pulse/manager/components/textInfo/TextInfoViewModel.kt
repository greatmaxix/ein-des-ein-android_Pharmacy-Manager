package com.pulse.manager.components.textInfo

import com.pulse.manager.components.textInfo.repository.TextInfoRepository
import com.pulse.manager.core.base.mvvm.BaseViewModel
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class TextInfoViewModel(private val repository: TextInfoRepository, val args: TextInfoFragmentArgs) : BaseViewModel()