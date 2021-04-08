package com.pulse.manager.components.signIn

import androidx.lifecycle.viewModelScope
import com.pulse.manager.components.signIn.SignInFragmentDirections.Companion.globalToHome
import com.pulse.manager.components.signIn.repository.SignInRepository
import com.pulse.manager.core.base.mvvm.BaseViewModel
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class SignInViewModel(private val repository: SignInRepository) : BaseViewModel() {

    fun performSignIn(email: String, password: String) = viewModelScope.execute {
        repository.login(email, password)
        navEvent.postEvent(globalToHome())
    }
}