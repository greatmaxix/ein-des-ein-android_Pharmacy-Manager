package com.pharmacy.manager.components.signIn

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import com.pharmacy.manager.R
import com.pharmacy.manager.components.signIn.SignInFragmentDirections.Companion.globalToHome
import com.pharmacy.manager.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.manager.core.extensions.*
import kotlinx.android.synthetic.main.fragment_sign_in.*
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class SignInFragment(private val vm: SignInViewModel) : BaseMVVMFragment(R.layout.fragment_sign_in) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        llButtonContainer.isEnabled = false
        var isEmailAccepted = false
        var isPasswordAccepted = false
        etEmailSignIn.doAfterTextChanged {
            val isEmailAddressValid = it.toString().isEmail()
            isEmailAccepted = tilEmailSignIn.changeFieldState(isEmailAccepted, isEmailAddressValid, null)
            updateSignInButtonState(isEmailAccepted && isPasswordAccepted)
        }
        etPasswordSignIn.doAfterTextChanged {
            val isPasswordValid = it.toString().isPasswordLength
            isPasswordAccepted = tilPasswordSignIn.changeFieldState(isPasswordAccepted, isPasswordValid, null)
            updateSignInButtonState(isEmailAccepted && isPasswordAccepted)
        }
        llButtonContainer.setDebounceOnClickListener {
            observeResult(vm.performSignIn(etEmailSignIn.text?.toTrim.orEmpty(), etPasswordSignIn.text?.toTrim.orEmpty())) {
                onEmmit = { navController.navigate(globalToHome()) }
            }
        }

        debug {
            // TODO Test data - pharmacist1@example.com - pharmacist3@example.com (pw1234)
            etEmailSignIn.setText("pharmacist1@example.com")
            etPasswordSignIn.setText("pw1234")
        }
    }

    private fun updateSignInButtonState(isEnabled: Boolean) {
        llButtonContainer.isEnabled = isEnabled
    }
}