package com.pulse.manager.components.profile

import androidx.lifecycle.LiveData
import com.pulse.manager.components.profile.repository.ProfileRepository
import com.pulse.manager.components.signIn.model.User
import com.pulse.manager.core.base.mvvm.BaseViewModel
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class ProfileViewModel(private val repository: ProfileRepository) : BaseViewModel() {

    val profileLiveData: LiveData<User> by lazy { repository.getUser() }

    fun logout() = requestLiveData {
        repository.logout()
    }
}