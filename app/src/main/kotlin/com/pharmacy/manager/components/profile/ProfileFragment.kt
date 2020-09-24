package com.pharmacy.manager.components.profile

import android.os.Bundle
import android.view.View
import com.pharmacy.manager.R
import com.pharmacy.manager.components.profile.ProfileFragmentDirections.Companion.actionFromProfileToSplash
import com.pharmacy.manager.components.profile.ProfileFragmentDirections.Companion.fromProfileToAboutApp
import com.pharmacy.manager.components.profile.ProfileFragmentDirections.Companion.fromProfileToNeedHelp
import com.pharmacy.manager.components.profile.ProfileFragmentDirections.Companion.fromProfileToNotifications
import com.pharmacy.manager.core.base.fragment.dialog.AlertDialogFragment
import com.pharmacy.manager.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.manager.core.extensions.loadCircularImage
import kotlinx.android.synthetic.main.fragment_profile.*

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
        observeRestResult<Unit> {
            liveData = vm.logout()
            onEmmit = {
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

            // TODO set proper data
            ivAvatarProfile.loadCircularImage("https://www.nj.com/resizer/h8MrN0-Nw5dB5FOmMVGMmfVKFJo=/450x0/smart/cloudfront-us-east-1.images.arcpublishing.com/advancelocal/SJGKVE5UNVESVCW7BBOHKQCZVE.jpg")
            tvRatingProfile.text = getString(R.string.ratingHolder, 4.2)
        }
    }
}