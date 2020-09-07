package com.pharmacy.manager.components.signIn

import androidx.lifecycle.LiveData
import androidx.navigation.NavDirections
import com.pharmacy.manager.components.signIn.SignInFragmentDirections.Companion.globalToHome
import com.pharmacy.manager.components.signIn.repository.SignInRepository
import com.pharmacy.manager.core.base.mvvm.BaseViewModel
import com.pharmacy.manager.core.general.SingleLiveEvent
import kotlinx.coroutines.delay

class SignInViewModel(private val repository: SignInRepository) : BaseViewModel() {

    private val _directionLiveData by lazy { SingleLiveEvent<NavDirections>() }
    val directionLiveData: LiveData<NavDirections> by lazy { _directionLiveData }

    fun performSignIn(email: String, password: String) {
        launchIO {
            delay(1000) // TODO stub
            _directionLiveData.postValue(globalToHome())
        }
    }
}