package com.pulse.manager.components.splash

import androidx.lifecycle.LiveData
import androidx.navigation.NavDirections
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.pulse.manager.components.splash.SplashFragmentDirections.Companion.fromSplashToSignIn
import com.pulse.manager.components.splash.SplashFragmentDirections.Companion.globalToHome
import com.pulse.manager.components.splash.repository.SplashRepository
import com.pulse.manager.core.base.mvvm.BaseViewModel
import com.pulse.manager.core.general.SingleLiveEvent
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

    private val _directionLiveData by lazy { SingleLiveEvent<NavDirections>() }
    val directionLiveData: LiveData<NavDirections> by lazy { _directionLiveData }

    fun checkAuthentication() {
        launchIO {
            if (repository.isUserLoggedIn) {
                workManager.enqueue(get<OneTimeWorkRequest>(named(UPDATE_PROFILE_INFO))).state
            }
            delay(DELAY)
            _directionLiveData.postValue(repository.isUserLoggedIn.toNavDirection)
        }
    }

    private val Boolean.toNavDirection
        get() = if (this) globalToHome() else fromSplashToSignIn()

    companion object {

        private const val DELAY = 1000L
        const val UPDATE_PROFILE_INFO = "updateCustomerInfo"
    }
}