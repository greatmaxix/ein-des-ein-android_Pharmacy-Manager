package com.pulse.manager.components.about

import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.manager.BuildConfig
import com.pulse.manager.R
import com.pulse.manager.components.about.AboutAppFragmentDirections.Companion.fromAboutAppToTextInfo
import com.pulse.manager.components.textInfo.TextInfoFragment
import com.pulse.manager.core.base.fragment.BaseToolbarFragment
import com.pulse.manager.databinding.FragmentAboutAppBinding
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class AboutAppFragment : BaseToolbarFragment<AboutAppViewModel>(R.layout.fragment_about_app, AboutAppViewModel::class) {

    private val binding by viewBinding(FragmentAboutAppBinding::bind)

    override fun initUI() = with(binding) {
        showBackButton()

        // TODO set proper data
        mtvVersion.text = getString(R.string.versionHolder, BuildConfig.VERSION_NAME)
        mtvAddress.text = "1208742, г. Астана, ул. Бейбитшилик, 33, подьезд 6, этаж 5, помещение 730"
        mtvSite.text = "kzpharamcy.com"

        itemAgreement.setOnClick { navController.navigate(fromAboutAppToTextInfo(TextInfoFragment.KEY_USER_AGREEMENT)) }
        itemPrivacy.setOnClick { navController.navigate(fromAboutAppToTextInfo(TextInfoFragment.KEY_PRIVACY_POLICY)) }
        itemTerms.setOnClick { navController.navigate(fromAboutAppToTextInfo(TextInfoFragment.KEY_TERMS_AND_CONDITIONS)) }
        itemCashback.setOnClick { navController.navigate(fromAboutAppToTextInfo(TextInfoFragment.KEY_CASHBACK)) }
    }
}