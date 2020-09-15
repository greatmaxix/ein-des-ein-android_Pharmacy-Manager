package com.pharmacy.manager.components.startUp.repository

import com.pharmacy.manager.data.local.SPManager

class StartUpLocalDataSource(private val sp: SPManager) {

    val isUserLoggedIn
        get() = sp.accessToken.isNotBlank()
}