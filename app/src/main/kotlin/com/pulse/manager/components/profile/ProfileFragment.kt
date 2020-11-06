package com.pulse.manager.components.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pulse.manager.R
import com.pulse.manager.components.mercureService.MercureEventListenerService
import com.pulse.manager.components.profile.ProfileFragmentDirections.Companion.actionFromProfileToSplash
import com.pulse.manager.components.profile.ProfileFragmentDirections.Companion.fromProfileToAboutApp
import com.pulse.manager.components.profile.ProfileFragmentDirections.Companion.fromProfileToNeedHelp
import com.pulse.manager.components.profile.ProfileFragmentDirections.Companion.fromProfileToNotifications
import com.pulse.manager.core.base.fragment.dialog.AlertDialogFragment
import com.pulse.manager.core.base.mvvm.BaseMVVMFragment
import com.pulse.manager.core.extensions.isServiceRunning
import com.pulse.manager.core.extensions.loadCircularImage
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class ProfileFragment(private val vm: ProfileViewModel) : BaseMVVMFragment(R.layout.fragment_profile) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        itemStatisticsProfile.setOnClick {
//          TODO uncomment in future and set proper color in xml
//        }
        itemNotificationsProfile.setOnClick {
            navController.navigate(fromProfileToNotifications())
        }
        itemAboutAppProfile.setOnClick {
            navController.navigate(fromProfileToAboutApp())
        }
        itemHelpProfile.setOnClick {
            navController.navigate(fromProfileToNeedHelp())
        }
        itemLogoutProfile.setOnClick {
            AlertDialogFragment.newInstance(
                getString(R.string.areYouSureWantExit),
                getString(R.string.logoutDescription),
                getString(R.string.logout),
                getString(R.string.common_cancelButton)
            ).apply {
                setNegativeListener { dialog, _ -> dialog.dismiss() }
                setPositiveListener { _, _ -> performLogout() }
                isCancelable = true
            }.show(childFragmentManager)
        }
    }

    private fun performLogout() {
        observeResult(vm.logout()) {
            onEmmit = {
                if (!requireContext().isServiceRunning(MercureEventListenerService::class.java)) {
                    requireContext().stopService(Intent(requireContext(), MercureEventListenerService::class.java))
                }
                navController.navigate(actionFromProfileToSplash())
            }
        }
    }

    override fun onBindLiveData() {
        super.onBindLiveData()

        observe(vm.profileLiveData) {
            val fullName = "$firstName $lastName"
            tvNameProfile.text = fullName
            tvEmailProfile.text = email
            ivAvatarProfile.loadCircularImage(avatar?.url)
            tvRatingProfile.text = getString(R.string.ratingHolder, chatRatingInfo?.rating ?: 0.0f)
        }
    }
}