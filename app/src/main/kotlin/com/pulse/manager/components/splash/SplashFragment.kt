package com.pulse.manager.components.splash

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.pulse.manager.R
import com.pulse.manager.core.base.mvvm.BaseMVVMFragment
import com.pulse.manager.core.extensions.animateVisible
import kotlinx.android.synthetic.main.fragment_splash.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class SplashFragment(private val vm: SplashViewModel) :
    BaseMVVMFragment(R.layout.fragment_splash) {

    private val duration by lazy { resources.getInteger(R.integer.animation_time).toLong() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.checkAuthentication()
        llLogo.animateVisible(duration)
        setupMainBackground()
    }

    override fun onBindLiveData() {
        super.onBindLiveData()
        observe(vm.directionLiveData, navController::navigate)
    }

    // App theme have image background, will change after Splash screen to main background color
    private fun setupMainBackground() {
        viewLifecycleOwner.lifecycleScope.launch {
            delay(BACKGROUND_CHANGE_DELAY)
            requireActivity().window.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.background_main
                )
            )
        }
    }

    companion object {

        private const val BACKGROUND_CHANGE_DELAY = 100L
    }
}