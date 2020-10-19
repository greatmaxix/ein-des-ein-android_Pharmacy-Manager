package com.pharmacy.manager.components.signIn

import com.pharmacy.manager.components.signIn.repository.SignInRepository
import com.pharmacy.manager.core.base.mvvm.BaseViewModel
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class SignInViewModel(private val repository: SignInRepository) : BaseViewModel() {

    fun performSignIn(email: String, password: String) = requestLiveData {
        repository.login(email, password)
    }
}