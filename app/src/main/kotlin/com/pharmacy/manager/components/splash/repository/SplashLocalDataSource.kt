package com.pharmacy.manager.components.splash.repository

import com.pharmacy.manager.data.local.SPManager

class SplashLocalDataSource(private val sp: SPManager) {

    val isUserLoggedIn
        get() = sp.accessToken.isNotBlank()
}