package com.pulse.manager.core.base

import android.content.Intent
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.pulse.manager.R
import com.pulse.manager.core.general.behavior.IBehavior

abstract class BaseActivity(@LayoutRes layoutResourceId: Int) :
    AppCompatActivity(layoutResourceId) {

    companion object {
        const val ANIM_EXIT = R.anim.anim_activity_exit
        const val ANIM_ENTER = R.anim.anim_activity_enter
    }

    private val behaviors = mutableListOf<IBehavior?>()

    protected val navController: NavController by lazy {
        try {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
            navHostFragment.navController
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("${this::class.java.simpleName} does not use \"navController\"")
        }
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(ANIM_ENTER, ANIM_EXIT)
    }

    @CallSuper
    override fun finish() {
        super.finish()
        overridePendingTransition(ANIM_ENTER, ANIM_EXIT)
    }

    @CallSuper
    override fun startActivity(intent: Intent?) {
        super.startActivity(intent)
        overridePendingTransition(ANIM_ENTER, ANIM_EXIT)
    }

    @CallSuper
    override fun onDestroy() {
        behaviors.forEach { it?.detach() }
        behaviors.clear()
        super.onDestroy()
    }

    protected fun <T : IBehavior> attachBehavior(behavior: T) = behavior.also {
        behaviors.add(it)
    }
}