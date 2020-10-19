package com.pharmacy.manager.components.textInfo

import android.os.Bundle
import android.view.View
import com.pharmacy.manager.R
import com.pharmacy.manager.core.base.mvvm.BaseMVVMFragment
import kotlinx.android.synthetic.main.fragment_text_info.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.core.parameter.parametersOf

@KoinApiExtension
class TextInfoFragment : BaseMVVMFragment(R.layout.fragment_text_info) {

    private val vm: TextInfoViewModel by viewModel(parameters = { parametersOf(TextInfoFragmentArgs.fromBundle(requireArguments())) })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton()

        val titleResId = when (vm.args.infoType) {
            KEY_USER_AGREEMENT -> R.string.userAgreement
            KEY_PRIVACY_POLICY -> R.string.privacyPolicy
            KEY_TERMS_AND_CONDITIONS -> R.string.termsAndConditions
            else -> R.string.cashback // TODO else is wrong
        }
        toolbar?.setTitle(titleResId)

        textInfo.setText(titleResId) // TODO remove
    }

    companion object {

        const val KEY_USER_AGREEMENT = 1
        const val KEY_PRIVACY_POLICY = 2
        const val KEY_TERMS_AND_CONDITIONS = 3
        const val KEY_CASHBACK = 4
    }
}