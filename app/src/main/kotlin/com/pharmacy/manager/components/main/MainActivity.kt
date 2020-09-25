package com.pharmacy.manager.components.main

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.navigation.NavDestination
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pharmacy.manager.R
import com.pharmacy.manager.core.base.BaseActivity
import com.pharmacy.manager.core.extensions.*
import com.pharmacy.manager.core.general.behavior.MessagesBehavior
import com.pharmacy.manager.core.general.behavior.ProgressViewBehavior
import com.pharmacy.manager.core.general.interfaces.MessagesCallback
import com.pharmacy.manager.core.general.interfaces.ProgressCallback
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_progress.*

class MainActivity : BaseActivity(R.layout.activity_main), ProgressCallback, MessagesCallback {

    private val progressBehavior by lazy { attachBehavior(ProgressViewBehavior(progress)) }
    private val messagesBehavior by lazy { attachBehavior(MessagesBehavior(this)) }
    private val topLevelDestinations = intArrayOf(R.id.nav_home, R.id.nav_profile) // TODO add destinations
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
        bottomNavMain.itemIconTintList = null
        setupWithNavController(navController)
        setOnNavigationItemReselectedListener {}
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.isTopLevelDestination) showNavViews() else hideNavViews()
            changeIcons(destination)
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
        ivChatMain.animateVisibleIfNot()
        bottomNavMain.translateYUp()
        ivChatMain.translateYUp()
    }

    private fun hideNavViews() {
        bottomNavMain.translateYDown()
        ivChatMain.translateYDown()
        ivChatMain.animateGoneIfNot()
    }

    private fun BottomNavigationView.changeIcons(destination: NavDestination) {
        bottomNavMain.menu.findItem(R.id.nav_home)?.icon =
            ContextCompat.getDrawable(context, if (destination.isTopDestinationAndHome) R.drawable.ic_home_blue else R.drawable.ic_home)
        val border = if (destination.id == R.id.nav_profile) resources.getDimensionPixelSize(R.dimen._2sdp).toFloat() else 0f
        Glide.with(context)
            .asBitmap()
            .load("https://www.nj.com/resizer/h8MrN0-Nw5dB5FOmMVGMmfVKFJo=/450x0/smart/cloudfront-us-east-1.images.arcpublishing.com/advancelocal/SJGKVE5UNVESVCW7BBOHKQCZVE.jpg") // TODO set image from profile
            .apply(RequestOptions.circleCropTransform())
            .into(object : CustomTarget<Bitmap>(128, 128) {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    bottomNavMain.menu.findItem(R.id.graph_profile)?.icon = resource.run {
                        RoundedBitmapDrawableFactory.create(
                            resources, if (border > 0) {
                                createBitmapWithBorder(border, context.compatColor(R.color.primaryBlue))
                            } else {
                                this
                            }
                        ).apply {
                            isCircular = true
                        }
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // no op
                }
            })
    }
}