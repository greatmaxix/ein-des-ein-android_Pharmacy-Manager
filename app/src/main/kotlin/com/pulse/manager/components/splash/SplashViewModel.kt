package com.pulse.manager.components.splash

import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.pulse.manager.components.splash.SplashFragmentDirections.Companion.fromSplashToSignIn
import com.pulse.manager.components.splash.SplashFragmentDirections.Companion.globalToHome
import com.pulse.manager.components.splash.repository.SplashRepository
import com.pulse.manager.core.base.mvvm.BaseViewModel
import kotlinx.coroutines.delay
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.qualifier.named

@KoinApiExtension
class SplashViewModel(
    private val repository: SplashRepository,
    private val workManager: WorkManager
) : BaseViewModel(), KoinComponent {

    fun checkAuthentication() = viewModelScope.execute {
        if (repository.isUserLoggedIn) {
            workManager.enqueue(get<OneTimeWorkRequest>(named(UPDATE_PROFILE_INFO))).state
        }
        delay(DELAY)
        navEvent.postEvent(repository.isUserLoggedIn.toNavDirection)
    }

    private val Boolean.toNavDirection
        get() = if (this) globalToHome() else fromSplashToSignIn()

    companion object {

        private const val DELAY = 1000L
        const val UPDATE_PROFILE_INFO = "updateCustomerInfo"
    }
}