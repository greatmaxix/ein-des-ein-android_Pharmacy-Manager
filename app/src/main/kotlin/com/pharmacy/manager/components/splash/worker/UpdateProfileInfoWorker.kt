package com.pharmacy.manager.components.splash.worker

import android.content.Context
import androidx.work.WorkerParameters
import com.pharmacy.manager.components.splash.repository.SplashRepository
import com.pharmacy.manager.core.base.worker.BaseCoroutineWorker
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import org.koin.core.inject

class UpdateProfileInfoWorker(context: Context, params: WorkerParameters) : BaseCoroutineWorker(context, params) {

    private val repository by inject<SplashRepository>()

    override suspend fun doWork() = withContext(IO) {
        try {
            repository.fetchUser()
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}