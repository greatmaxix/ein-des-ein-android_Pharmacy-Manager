package com.pharmacy.manager.components.main

import android.os.Bundle
import android.view.WindowManager
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
import com.pharmacy.manager.widget.SelectableBottomNavView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_progress.*

class MainActivity : BaseActivity(R.layout.activity_main), ProgressCallback, MessagesCallback {

    private val progressBehavior by lazy { attachBehavior(ProgressViewBehavior(progress)) }
    private val messagesBehavior by lazy { attachBehavior(MessagesBehavior(this)) }
    private val topLevelDestinations = intArrayOf(R.id.nav_home, R.id.nav_categories, R.id.nav_chat_list, R.id.nav_profile) // TODO add destinations
    private val NavDestination.isTopLevelDestination
        get() = topLevelDestinations.contains(id)
    private val NavDestination.isTopDestinationAndHome
        get() = isTopLevelDestination && id == R.id.nav_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupNavigation()
    }

    private fun setupNavigation() = with(bottomNavMain) {
        setTopRoundCornerBackground()
        itemIconTintList = null
        // TODO get avatar from profile
        val avatar = "https://www.nj.com/resizer/h8MrN0-Nw5dB5FOmMVGMmfVKFJo=/450x0/smart/cloudfront-us-east-1.images.arcpublishing.com/advancelocal/SJGKVE5UNVESVCW7BBOHKQCZVE.jpg"
        navItems = listOf(
            SelectableBottomNavView.NavItem(R.id.nav_home, R.id.nav_home, R.drawable.ic_home, null),
            SelectableBottomNavView.NavItem(R.id.nav_categories, R.id.nav_categories, R.drawable.ic_category, null),
            SelectableBottomNavView.NavItem(R.id.nav_chat_list, R.id.nav_chat_list, R.drawable.ic_chat, null),
            SelectableBottomNavView.NavItem(R.id.graph_profile, R.id.nav_profile, null, avatar)
        )
        setupWithNavController(navController)
        setOnNavigationItemReselectedListener {}
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.isTopLevelDestination) bottomNavMain.translateYUp() else bottomNavMain.translateYDown()
            if (destination.isTopDestinationAndHome) window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) else window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            changeSelection(destination)
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
}