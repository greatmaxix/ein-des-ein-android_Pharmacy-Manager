package com.pharmacy.manager.components.main

import com.pharmacy.manager.R
import com.pharmacy.manager.core.base.BaseActivity
import com.pharmacy.manager.core.general.behavior.MessagesBehavior
import com.pharmacy.manager.core.general.behavior.ProgressViewBehavior
import com.pharmacy.manager.core.general.interfaces.MessagesCallback
import com.pharmacy.manager.core.general.interfaces.ProgressCallback
import kotlinx.android.synthetic.main.layout_progress.*

class MainActivity : BaseActivity(R.layout.activity_main), ProgressCallback, MessagesCallback {

    private val progressBehavior by lazy { attachBehavior(ProgressViewBehavior(progress)) }
    private val messagesBehavior by lazy { attachBehavior(MessagesBehavior(this)) }
//    private val topLevelDestinations = intArrayOf(R.id.nav_home) // TODO fill

    override fun setInProgress(progress: Boolean) = progressBehavior.setInProgress(progress)

    override fun showError(error: String, action: (() -> Unit)?) =
        messagesBehavior.showError(error, action)

    override fun showError(strResId: Int, action: (() -> Unit)?) =
        messagesBehavior.showError(strResId, action)

    override fun onBackPressed() {
        navController.currentDestination?.apply {
            when {
                // TODO fill

//                isTopDestinationAndHome -> finish()
//                isTopLevelDestination -> navController.navigate(R.id.graph_home)
                else -> super.onBackPressed()
            }
        } ?: super.onBackPressed()
    }
// TODO fill

//    @SuppressLint("RestrictedApi")
//    private fun setupNavigation() = with(bottomNavigationView) {
//        setupWithNavController(navController)
//        setOnNavigationItemReselectedListener {}
//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            if (destination.isTopLevelDestination) {
//                showNavViews()
//                fabCart.setBorderEnabled(destination.id == R.id.nav_cart)
//            } else hideNavViews()
//        }
//        fabCart.setDebounceOnClickListener { navController.navigate(R.id.graph_cart) }
//        fabCart.onGlobalLayout { BadgeUtils.attachBadgeDrawable(fabBadge, fabCart, null) }
//    }
//
//    private val NavDestination.isTopLevelDestination
//        get() = topLevelDestinations.contains(id)
//
//    private val NavDestination.isTopDestinationAndHome
//        get() = isTopLevelDestination && id == R.id.nav_home
}