package com.pulse.manager.components.main

import android.content.Context
import android.content.Intent
import androidx.annotation.IdRes
import androidx.annotation.NavigationRes
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.NavDestination
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.manager.R
import com.pulse.manager.components.mercureService.MercureEventListenerService.Companion.EXTRA_CHAT_ID
import com.pulse.manager.core.base.mvvm.BaseMVVMActivity
import com.pulse.manager.core.extensions.observe
import com.pulse.manager.core.extensions.setTopRoundCornerBackground
import com.pulse.manager.core.extensions.translateYDown
import com.pulse.manager.core.extensions.translateYUp
import com.pulse.manager.core.locale.ILocaleManager
import com.pulse.manager.databinding.ActivityMainBinding
import com.pulse.manager.widget.SelectableBottomNavView
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map
import org.koin.android.ext.android.inject
import org.koin.core.component.KoinApiExtension
import timber.log.Timber

@KoinApiExtension
class MainActivity : BaseMVVMActivity<MainViewModel>(R.layout.activity_main, MainViewModel::class) {

    private val binding by viewBinding(ActivityMainBinding::bind, R.id.cl_container)
    private val localeManager by inject<ILocaleManager>()
    private val topLevelDestinations = intArrayOf(R.id.nav_home, R.id.nav_categories, R.id.nav_chat_list, R.id.nav_profile) // TODO add destinations
    private val NavDestination.isTopLevelDestination
        get() = topLevelDestinations.contains(id)
    private val NavDestination.isTopDestinationAndHome
        get() = isTopLevelDestination && id == R.id.nav_home
    private val NavDestination.isCategories
        get() = id == R.id.nav_categories

    override fun initUI() {
        setupNavigation()
        checkIntentChatId(intent)
    }

    override fun onBindStates() = with(lifecycleScope) {
        observe(viewModel.userFlow) {
            it?.let {
                binding.bottomNavigation.navItems = listOf(
                    SelectableBottomNavView.NavItem(R.id.nav_home, R.id.nav_home, R.drawable.ic_home, false, null),
                    SelectableBottomNavView.NavItem(R.id.nav_categories, R.id.nav_categories, R.drawable.ic_category, false, null),
                    SelectableBottomNavView.NavItem(R.id.nav_chat_list, R.id.nav_chat_list, R.drawable.ic_chat, false, null),
                    SelectableBottomNavView.NavItem(R.id.graph_profile, R.id.nav_profile, null, true, it.avatar?.url)
                )
            }
        }
        observe(localeManager.appLocaleFlow
            .distinctUntilChanged()
            .drop(1)
            .map { R.navigation.graph_profile to R.id.nav_language }
        ) { notifyContextChanged(it.first, it.second) }
    }

    private fun checkIntentChatId(intent: Intent?) {
        intent?.extras?.let {
            val chatId = it.getInt(EXTRA_CHAT_ID, -1)
            if (chatId != -1) viewModel.goToChat(chatId)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        checkIntentChatId(intent)
    }

    override fun attachBaseContext(newBase: Context) = super.attachBaseContext(localeManager.createLocalisedContext(newBase))

    private fun setupNavigation() = with(binding.bottomNavigation) {
        setTopRoundCornerBackground()
        itemIconTintList = null
        navItems = listOf(
            SelectableBottomNavView.NavItem(R.id.nav_home, R.id.nav_home, R.drawable.ic_home, false, null),
            SelectableBottomNavView.NavItem(R.id.nav_categories, R.id.nav_categories, R.drawable.ic_category, false, null),
            SelectableBottomNavView.NavItem(R.id.nav_chat_list, R.id.nav_chat_list, R.drawable.ic_chat, false, null),
            SelectableBottomNavView.NavItem(R.id.graph_profile, R.id.nav_profile, null, true, null)
        )

        setupWithNavController(navController)
        setOnNavigationItemReselectedListener {}
        navController.addOnDestinationChangedListener { _, destination, _ ->
            Timber.d("Destination ID > ${resources.getResourceEntryName(destination.id)}")
            if (destination.isTopLevelDestination) translateYUp() else translateYDown()
            viewModel.setChatForeground(destination.id == R.id.nav_chat)
            changeSelection(destination.id)
        }
    }

    override fun onBackPressed() {
        navController.currentDestination?.apply {
            when {
                isCategories -> super.onBackPressed()
                isTopDestinationAndHome -> finish()
                isTopLevelDestination -> navController.navigate(R.id.nav_home)
                else -> super.onBackPressed()
            }
        } ?: super.onBackPressed()
    }

    private fun notifyContextChanged(@NavigationRes navGraphId: Int, @IdRes destId: Int) {
        NavDeepLinkBuilder(this)
            .setGraph(navGraphId)
            .setDestination(destId)
            .createTaskStackBuilder()
            .startActivities()
    }
}