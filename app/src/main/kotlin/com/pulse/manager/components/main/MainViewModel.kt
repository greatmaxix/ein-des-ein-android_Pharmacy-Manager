package com.pulse.manager.components.main

import com.pulse.manager.components.main.repository.MainRepository
import com.pulse.manager.core.base.mvvm.BaseViewModel
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class MainViewModel(private val repository: MainRepository) : BaseViewModel() {

    val userLiveData = repository.getUserLiveData()

    fun setChatForeground(isForeground: Boolean) {
        repository.setChatForeground(isForeground)
    }

    fun goToChat(chatId: Int) = requestLiveData {
        repository.getChat(chatId)
    }
}