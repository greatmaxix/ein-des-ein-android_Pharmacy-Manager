package com.pharmacy.manager.components.startUp

import androidx.lifecycle.liveData
import com.pharmacy.manager.components.startUp.repository.StartUpRepository
import com.pharmacy.manager.core.base.mvvm.BaseViewModel
import kotlinx.coroutines.delay

class StartUpViewModel(private val repository: StartUpRepository) : BaseViewModel() {

    val isUserLoggedInLiveData by lazy {
        liveData {
            delay(STARTUP_DELAY)
            emit(repository.isUserLoggedIn)
        }
    }

    companion object {

        private const val STARTUP_DELAY = 3000L
    }
}