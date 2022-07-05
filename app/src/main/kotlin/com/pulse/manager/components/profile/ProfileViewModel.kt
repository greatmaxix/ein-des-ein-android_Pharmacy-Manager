package com.pulse.manager.components.profile

import androidx.lifecycle.viewModelScope
import com.pulse.core.utils.flow.SingleShotEvent
import com.pulse.manager.components.profile.repository.ProfileRepository
import com.pulse.manager.core.base.mvvm.BaseViewModel
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class ProfileViewModel(private val repository: ProfileRepository) : BaseViewModel() {

    val profileState = repository.getUser()
    val logoutEvent = SingleShotEvent<Unit>()

    fun logout() = viewModelScope.execute {
        repository.logout()
        logoutEvent.offerEvent(Unit)
    }
}