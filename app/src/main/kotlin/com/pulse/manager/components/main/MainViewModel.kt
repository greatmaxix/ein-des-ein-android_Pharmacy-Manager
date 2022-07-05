package com.pulse.manager.components.main

import androidx.lifecycle.viewModelScope
import com.pulse.manager.GraphMainDirections.Companion.globalToChat
import com.pulse.manager.components.main.repository.MainRepository
import com.pulse.manager.core.base.mvvm.BaseViewModel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class MainViewModel(private val repository: MainRepository) : BaseViewModel() {

    val userFlow = repository.getUserFlow()

    fun setChatForeground(isForeground: Boolean) = viewModelScope.launch {
        repository.setChatForeground(isForeground)
    }

    fun goToChat(chatId: Int) = viewModelScope.execute {
        val chat = repository.getChat(chatId)
        navEvent.postEvent(globalToChat(chat))
    }
}