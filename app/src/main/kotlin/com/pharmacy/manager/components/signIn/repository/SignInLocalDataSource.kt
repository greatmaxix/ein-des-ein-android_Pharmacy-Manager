package com.pharmacy.manager.components.signIn.repository

import com.pharmacy.manager.data.local.SPManager

class SignInLocalDataSource(private val sp: SPManager) {

    fun setUserLoggedIn() {
        sp.accessToken = "Some access token" // TODO change
    }
}