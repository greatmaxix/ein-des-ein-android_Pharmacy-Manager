package com.pharmacy.manager.components.main

import android.os.Bundle
import androidx.navigation.NavDestination
import androidx.navigation.ui.setupWithNavController
import com.pharmacy.manager.R
import com.pharmacy.manager.core.base.BaseActivity
import com.pharmacy.manager.core.extensions.setTopRoundCornerBackground
import com.pharmacy.manager.core.extensions.translateYDown
import com.pharmacy.manager.core.extensions.translateYUp
import com.pharmacy.manager.core.general.behavior.MessagesBehavior
import com.pharmacy.manager.core.general.behavior.ProgressViewBehavior
import com.pharmacy.manager.core.general.interfaces.MessagesCallback
import com.pharmacy.manager.core.general.interfaces.ProgressCallback
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_progress.*

class MainActivity : BaseActivity(R.layout.activity_main), ProgressCallback, MessagesCallback {

    private val progressBehavior by lazy { attachBehavior(ProgressViewBehavior(progress)) }
    private val messagesBehavior by lazy { attachBehavior(MessagesBehavior(this)) }
    private val topLevelDestinations = intArrayOf(R.id.nav_home) // TODO add destinations
    private val NavDestination.isTopLevelDestination
        get() = topLevelDestinations.contains(id)
    private val NavDestination.isTopDestinationAndHome
        get() = isTopLevelDestination && id == R.id.nav_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupNavigation()
    }

    private fun setupNavigation() = with(bottomNavMain) {
        bottomNavMain.setTopRoundCornerBackground()
        setupWithNavController(navController)
        setOnNavigationItemSelectedListener {
            true
        }
        setOnNavigationItemReselectedListener {}
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.isTopLevelDestination) showNavViews() else hideNavViews()
        }
        translationZ = 1f
    }

    override fun setInProgress(progress: Boolean) = progressBehavior.setInProgress(progress)

    override fun showError(error: String, action: (() -> Unit)?) =
        messagesBehavior.showError(error, action)

    override fun showError(strResId: Int, action: (() -> Unit)?) =
        messagesBehavior.showError(strResId, action)

    override fun onBackPressed() {
        navController.currentDestination?.apply {
            when {
                isTopDestinationAndHome -> finish()
                isTopLevelDestination -> navController.navigate(R.id.nav_home)
                else -> super.onBackPressed()
            }
        } ?: super.onBackPressed()
    }

    private fun showNavViews() {
        bottomNavMain.translateYUp()
        ivChatMain.translateYUp()
    }

    private fun hideNavViews() {
        bottomNavMain.translateYDown()
        ivChatMain.translateYDown()
    }
}