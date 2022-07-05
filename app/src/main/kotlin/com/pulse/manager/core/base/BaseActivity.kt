package com.pulse.manager.core.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.pulse.manager.R
import com.pulse.manager.core.extensions.lazyNavController

abstract class BaseActivity(@LayoutRes layoutResourceId: Int) : AppCompatActivity(layoutResourceId) {

    protected val navController by lazyNavController(R.id.nav_host)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUI()
    }

    /**
     * Here we may init UI components.
     * This method will be executed after parent [onCreate] method
     */
    protected abstract fun initUI()
}