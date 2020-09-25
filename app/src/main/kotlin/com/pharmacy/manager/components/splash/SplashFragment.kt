package com.pharmacy.manager.components.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.content.ContextCompat
import com.pharmacy.manager.R
import com.pharmacy.manager.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.manager.core.extensions.animateVisible
import kotlinx.android.synthetic.main.fragment_splash.*

class SplashFragment(private val vm: SplashViewModel) :
    BaseMVVMFragment(R.layout.fragment_splash) {

    private val duration by lazy { resources.getInteger(R.integer.animation_time).toLong() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.checkAuthentication()
        ivLogo.animateVisible(duration)
        setupMainBackground()
    }

    override fun onBindLiveData() {
        super.onBindLiveData()
        observe(vm.directionLiveData, navController::navigate)
    }

    // App theme have image background, will change after Splash screen to main background color
    private fun setupMainBackground() {
        Handler(Looper.getMainLooper()).postDelayed({
            requireActivity().window.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.background_main
                )
            )
        }, BACKGROUND_CHANGE_DELAY)
    }

    companion object {

        private const val BACKGROUND_CHANGE_DELAY = 100L
    }
}