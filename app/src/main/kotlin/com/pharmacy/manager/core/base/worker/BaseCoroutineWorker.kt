package com.pharmacy.manager.core.base.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.koin.core.KoinComponent

abstract class BaseCoroutineWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params), KoinComponent