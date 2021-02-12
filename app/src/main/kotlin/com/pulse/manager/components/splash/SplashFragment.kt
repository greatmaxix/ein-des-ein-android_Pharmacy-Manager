package com.pulse.manager.components.splash

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.manager.R
import com.pulse.manager.core.base.mvvm.BaseMVVMFragment
import com.pulse.manager.core.extensions.animateVisible
import com.pulse.manager.databinding.FragmentSplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class SplashFragment(private val viewModel: SplashViewModel) :
    BaseMVVMFragment(R.layout.fragment_splash) {

    private val binding by viewBinding(FragmentSplashBinding::bind)
    private val duration by lazy { resources.getInteger(R.integer.animation_time).toLong() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.checkAuthentication()
        llLogo.animateVisible(duration)
        setupMainBackground()
    }

    override fun onBindLiveData() {
        super.onBindLiveData()
        observe(viewModel.directionLiveData, navController::navigate)
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