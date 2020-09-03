package com.pharmacy.manager.components.startUp

import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import com.pharmacy.manager.R
import com.pharmacy.manager.core.base.mvvm.BaseMVVMFragment

class StartUpFragment(private val vm: StartUpViewModel) :
    BaseMVVMFragment(R.layout.fragment_startup) {

    override fun onBindLiveData() {
        observe(vm.isUserLoggedInLiveData, ::isUserLoggedIn)
    }

    private fun isUserLoggedIn(isLoggedIn: Boolean) {
        setupMainBackground()

        // TODO fill
//        if (isLoggedIn) {
//            navController.navigate(globalToHome())
//        } else {
//            mlRootStartUp.setTransition(R.id.transitionWelcome)
//            mlRootStartUp.transitionToEnd()
//        }
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