package com.pulse.manager.core.base.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent

@KoinApiExtension
abstract class BaseCoroutineWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params), KoinComponent