package com.pharmacy.manager.components.profile

import androidx.lifecycle.LiveData
import com.pharmacy.manager.components.profile.repository.ProfileRepository
import com.pharmacy.manager.components.signIn.model.User
import com.pharmacy.manager.core.base.mvvm.BaseViewModel

class ProfileViewModel(private val repository: ProfileRepository) : BaseViewModel() {

    val profileLiveData: LiveData<User> by lazy { repository.getUser() }

    fun logout() = requestLiveData {
        repository.logout()
    }
}