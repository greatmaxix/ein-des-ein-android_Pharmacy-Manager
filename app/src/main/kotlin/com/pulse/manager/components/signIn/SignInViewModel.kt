package com.pulse.manager.components.signIn

import com.pulse.manager.components.signIn.repository.SignInRepository
import com.pulse.manager.core.base.mvvm.BaseViewModel
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class SignInViewModel(private val repository: SignInRepository) : BaseViewModel() {

    fun performSignIn(email: String, password: String) = requestLiveData {
        repository.login(email, password)
    }
}