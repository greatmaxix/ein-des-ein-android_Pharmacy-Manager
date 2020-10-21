package com.pharmacy.manager.components.main

import com.pharmacy.manager.components.main.repository.MainRepository
import com.pharmacy.manager.core.base.mvvm.BaseViewModel
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class MainViewModel(private val repository: MainRepository) : BaseViewModel() {

    fun setChatForeground(isForeground: Boolean) {
        repository.setChatForeground(isForeground)
    }

    fun goToChat(chatId: Int) = requestLiveData {
        repository.getChat(chatId)
    }
}