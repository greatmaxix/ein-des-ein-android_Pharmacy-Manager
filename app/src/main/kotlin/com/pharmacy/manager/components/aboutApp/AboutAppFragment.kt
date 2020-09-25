package com.pharmacy.manager.components.aboutApp

import android.os.Bundle
import android.view.View
import com.pharmacy.manager.BuildConfig
import com.pharmacy.manager.R
import com.pharmacy.manager.components.aboutApp.AboutAppFragmentDirections.Companion.fromAboutAppToTextInfo
import com.pharmacy.manager.components.textInfo.TextInfoFragment
import com.pharmacy.manager.core.base.mvvm.BaseMVVMFragment
import kotlinx.android.synthetic.main.fragment_about_app.*

class AboutAppFragment(private val vm: AboutAppViewModel) : BaseMVVMFragment(R.layout.fragment_about_app) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton()

        // TODO set proper data
        tvVersionAboutApp.text = getString(R.string.versionHolder, BuildConfig.VERSION_NAME)
        tvAddressAboutApp.text = "1208742, г. Астана, ул. Бейбитшилик, 33, подьезд 6, этаж 5, помещение 730"
        tvSiteAboutApp.text = "kzpharamcy.com"

        itemAgreementAboutApp.setOnClick {
            navController.navigate(fromAboutAppToTextInfo(TextInfoFragment.KEY_USER_AGREEMENT))
        }
        itemPrivacyAboutApp.setOnClick {
            navController.navigate(fromAboutAppToTextInfo(TextInfoFragment.KEY_PRIVACY_POLICY))
        }
        itemTermsAboutApp.setOnClick {
            navController.navigate(fromAboutAppToTextInfo(TextInfoFragment.KEY_TERMS_AND_CONDITIONS))
        }
        itemCashbackAboutApp.setOnClick {
            navController.navigate(fromAboutAppToTextInfo(TextInfoFragment.KEY_CASHBACK))
        }
    }
}