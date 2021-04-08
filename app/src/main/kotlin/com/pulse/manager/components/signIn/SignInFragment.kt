package com.pulse.manager.components.signIn

import androidx.core.widget.doAfterTextChanged
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.manager.R
import com.pulse.manager.core.base.mvvm.BaseMVVMFragment
import com.pulse.manager.core.extensions.*
import com.pulse.manager.databinding.FragmentSignInBinding
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class SignInFragment : BaseMVVMFragment<SignInViewModel>(R.layout.fragment_sign_in, SignInViewModel::class) {

    private val binding by viewBinding(FragmentSignInBinding::bind)

    override fun initUI()  = with(binding) {
        llButtonContainer.isEnabled = false
        var isEmailAccepted = false
        var isPasswordAccepted = false
        etEmail.doAfterTextChanged {
            val isEmailAddressValid = it.toString().isEmail()
            isEmailAccepted = tilEmail.changeFieldState(isEmailAccepted, isEmailAddressValid, null)
            updateSignInButtonState(isEmailAccepted && isPasswordAccepted)
        }
        etPassword.doAfterTextChanged {
            val isPasswordValid = it.toString().isPasswordLength
            isPasswordAccepted = tilPassword.changeFieldState(isPasswordAccepted, isPasswordValid, null)
            updateSignInButtonState(isEmailAccepted && isPasswordAccepted)
        }
        llButtonContainer.setDebounceOnClickListener {
            viewModel.performSignIn(etEmail.text?.toTrim.orEmpty(), etPassword.text?.toTrim.orEmpty())
        }

        debug {
            // TODO Test data - pharmacist1@example.com - pharmacist3@example.com (pw1234)
            etEmail.setText("pharmacist1@example.com")
            etPassword.setText("pw1234")
        }
    }

    private fun updateSignInButtonState(isEnabled: Boolean) {
        binding.llButtonContainer.isEnabled = isEnabled
    }
}