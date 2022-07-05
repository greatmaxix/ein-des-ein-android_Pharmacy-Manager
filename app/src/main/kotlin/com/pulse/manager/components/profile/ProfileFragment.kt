package com.pulse.manager.components.profile

import android.content.Intent
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.manager.R
import com.pulse.manager.components.mercureService.MercureEventListenerService
import com.pulse.manager.components.profile.ProfileFragmentDirections.Companion.actionFromProfileToSplash
import com.pulse.manager.components.profile.ProfileFragmentDirections.Companion.fromProfileToAboutApp
import com.pulse.manager.components.profile.ProfileFragmentDirections.Companion.fromProfileToLanguage
import com.pulse.manager.components.profile.ProfileFragmentDirections.Companion.fromProfileToNeedHelp
import com.pulse.manager.components.profile.ProfileFragmentDirections.Companion.fromProfileToNotifications
import com.pulse.manager.components.profile.ProfileFragmentDirections.Companion.fromProfileToStatistic
import com.pulse.manager.core.base.fragment.dialog.AlertDialogFragment
import com.pulse.manager.core.base.mvvm.BaseMVVMFragment
import com.pulse.manager.core.extensions.isServiceRunning
import com.pulse.manager.core.extensions.loadCircularImage
import com.pulse.manager.core.extensions.observe
import com.pulse.manager.databinding.FragmentProfileBinding
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class ProfileFragment : BaseMVVMFragment<ProfileViewModel>(R.layout.fragment_profile, ProfileViewModel::class) {

    private val binding by viewBinding(FragmentProfileBinding::bind)

    override fun initUI() = with(binding) {
        itemStatistics.setOnClick { navController.navigate(fromProfileToStatistic()) }
        itemLanguage.setOnClick { navController.navigate(fromProfileToLanguage()) }
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
                setPositiveListener { _, _ -> viewModel.logout() }
                isCancelable = true
            }.show(childFragmentManager)
        }
    }

    override fun onBindEvents() = with(lifecycleScope) {
        observe(viewModel.logoutEvent.events) {
            if (!requireContext().isServiceRunning(MercureEventListenerService::class.java)) {
                requireContext().stopService(Intent(requireContext(), MercureEventListenerService::class.java))
            }
            navController.navigate(actionFromProfileToSplash())
        }
    }

    override fun onBindStates() = with(lifecycleScope) {
        observe(viewModel.profileState) {
            it?.let {
                val fullName = "${it.firstName} ${it.lastName}"
                with(binding) {
                    mtvName.text = fullName
                    mtvEmail.text = it.email
                    ivAvatar.loadCircularImage(it.avatar?.url)
                    mtvRating.text = getString(R.string.ratingHolder, it.chatRatingInfo?.rating ?: 0.0f)
                }
            }
        }
    }
}