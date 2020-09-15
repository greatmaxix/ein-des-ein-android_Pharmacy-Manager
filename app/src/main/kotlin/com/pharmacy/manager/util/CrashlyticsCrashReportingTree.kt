package com.pharmacy.manager.util

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

class CrashlyticsCrashReportingTree : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO) {
            return
        }
        val t = throwable ?: Exception(message)

        with(FirebaseCrashlytics.getInstance()) {
            setCustomKey(CRASHLYTICS_KEY_PRIORITY, priority)
            tag?.let { setCustomKey(CRASHLYTICS_KEY_TAG, it) }
            setCustomKey(CRASHLYTICS_KEY_MESSAGE, message)
            recordException(t)
        }
    }

    companion object {
        const val CRASHLYTICS_KEY_PRIORITY = "priority"
        const val CRASHLYTICS_KEY_TAG = "tag"
        const val CRASHLYTICS_KEY_MESSAGE = "message"
    }
}