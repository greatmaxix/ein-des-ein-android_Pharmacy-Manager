package com.pharmacy.manager.components.signIn

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import com.pharmacy.manager.R
import com.pharmacy.manager.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.manager.core.extensions.*
import kotlinx.android.synthetic.main.fragment_sign_in.*

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
            vm.performSignIn(etEmailSignIn.text?.toTrim.orEmpty(), etPasswordSignIn.text?.toTrim.orEmpty())
        }
    }

    private fun updateSignInButtonState(isEnabled: Boolean) {
        llButtonContainer.isEnabled = isEnabled
    }

    override fun onBindLiveData() {
        observe(vm.directionLiveData, navController::navigate)
    }
}