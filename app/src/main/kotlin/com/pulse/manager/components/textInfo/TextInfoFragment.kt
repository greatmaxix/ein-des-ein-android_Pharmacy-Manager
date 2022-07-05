package com.pulse.manager.components.textInfo

import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.manager.R
import com.pulse.manager.core.base.fragment.BaseToolbarFragment
import com.pulse.manager.databinding.FragmentTextInfoBinding
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class TextInfoFragment : BaseToolbarFragment<TextInfoViewModel>(R.layout.fragment_text_info, TextInfoViewModel::class) {

    private val args by navArgs<TextInfoFragmentArgs>()
    private val binding by viewBinding(FragmentTextInfoBinding::bind)

    override fun initUI() = with(binding) {
        showBackButton()

        val titleResId = when (args.infoType) {
            KEY_USER_AGREEMENT -> R.string.userAgreement
            KEY_PRIVACY_POLICY -> R.string.privacyPolicy
            KEY_TERMS_AND_CONDITIONS -> R.string.termsAndConditions
            else -> R.string.cashback // TODO else is wrong
        }
        toolbar.toolbar.setTitle(titleResId)

        mtvText.setText(titleResId) // TODO remove
    }

    companion object {

        const val KEY_USER_AGREEMENT = 1
        const val KEY_PRIVACY_POLICY = 2
        const val KEY_TERMS_AND_CONDITIONS = 3
        const val KEY_CASHBACK = 4
    }
}