package com.pharmacy.manager.components.aboutApp

import com.pharmacy.manager.components.aboutApp.repository.AboutAppRepository
import com.pharmacy.manager.core.base.mvvm.BaseViewModel
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class AboutAppViewModel(private val repository: AboutAppRepository) : BaseViewModel()