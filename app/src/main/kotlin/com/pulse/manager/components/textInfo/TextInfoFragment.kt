package com.pulse.manager.components.textInfo

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.manager.R
import com.pulse.manager.core.base.mvvm.BaseMVVMFragment
import com.pulse.manager.databinding.FragmentTextInfoBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.core.parameter.parametersOf

@KoinApiExtension
class TextInfoFragment : BaseMVVMFragment(R.layout.fragment_text_info) {

    private val binding by viewBinding(FragmentTextInfoBinding::bind)
    private val viewModel: TextInfoViewModel by viewModel(parameters = { parametersOf(TextInfoFragmentArgs.fromBundle(requireArguments())) })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton()

        val titleResId = when (viewModel.args.infoType) {
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