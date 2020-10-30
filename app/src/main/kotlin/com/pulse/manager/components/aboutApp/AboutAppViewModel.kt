package com.pulse.manager.components.aboutApp

import com.pulse.manager.components.aboutApp.repository.AboutAppRepository
import com.pulse.manager.core.base.mvvm.BaseViewModel
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class AboutAppViewModel(private val repository: AboutAppRepository) : BaseViewModel()