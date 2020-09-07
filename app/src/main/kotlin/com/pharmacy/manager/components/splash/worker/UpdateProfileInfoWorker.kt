package com.pharmacy.manager.components.splash.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.pharmacy.manager.components.splash.repository.SplashRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject

class UpdateProfileInfoWorker(private val context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params), KoinComponent {

    private val repository by inject<SplashRepository>()

    override suspend fun doWork() = withContext(IO) {
        // TODO implement user entity update
//        when (val avatarUrl = repository.updateCustomerInfo()) {
//            is Success -> {
//                val url = avatarUrl.value
//                if (!url.isNullOrEmpty()) saveAvatarToFile(context, url) else Result.failure()
//                Result.success()
//            }
//            is Error -> Result.failure()
//        }
        Result.success()
    }
}