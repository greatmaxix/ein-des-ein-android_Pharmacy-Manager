package com.pulse.manager.components.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.manager.R
import com.pulse.manager.components.mercureService.MercureEventListenerService
import com.pulse.manager.components.profile.ProfileFragmentDirections.Companion.actionFromProfileToSplash
import com.pulse.manager.components.profile.ProfileFragmentDirections.Companion.fromProfileToAboutApp
import com.pulse.manager.components.profile.ProfileFragmentDirections.Companion.fromProfileToNeedHelp
import com.pulse.manager.components.profile.ProfileFragmentDirections.Companion.fromProfileToNotifications
import com.pulse.manager.components.profile.ProfileFragmentDirections.Companion.fromProfileToStatistic
import com.pulse.manager.core.base.fragment.dialog.AlertDialogFragment
import com.pulse.manager.core.base.mvvm.BaseMVVMFragment
import com.pulse.manager.core.extensions.isServiceRunning
import com.pulse.manager.core.extensions.loadCircularImage
import com.pulse.manager.databinding.FragmentProfileBinding
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class ProfileFragment(private val viewModel: ProfileViewModel) : BaseMVVMFragment(R.layout.fragment_profile) {

    private val binding by viewBinding(FragmentProfileBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        itemStatistics.setOnClick { navController.navigate(fromProfileToStatistic()) }
        itemNotifications.setOnClick { navController.navigate(fromProfileToNotifications()) }
        itemAboutApp.setOnClick { navController.navigate(fromProfileToAboutApp()) }
        itemHelp.setOnClick { navController.navigate(fromProfileToNeedHelp()) }
        itemLogout.setOnClick {
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
        observeResult(viewModel.logout()) {
            onEmmit = {
                if (!requireContext().isServiceRunning(MercureEventListenerService::class.java)) {
                    requireContext().stopService(Intent(requireContext(), MercureEventListenerService::class.java))
                }
                navController.navigate(actionFromProfileToSplash())
            }
        }
    }

    override fun onBindLiveData() = with(binding) {
        super.onBindLiveData()

        observe(viewModel.profileLiveData) {
            val fullName = "$firstName $lastName"
            mtvName.text = fullName
            mtvEmail.text = email
            ivAvatar.loadCircularImage(avatar?.url)
            mtvRating.text = getString(R.string.ratingHolder, chatRatingInfo?.rating ?: 0.0f)
        }
    }
}