package com.pharmacy.manager.components.splash.repository

import com.pharmacy.manager.components.signIn.model.User
import com.pharmacy.manager.components.signIn.model.UserDAO
import com.pharmacy.manager.data.local.SPManager

class SplashLocalDataSource(private val sp: SPManager, private val userDAO: UserDAO) {

    val isUserLoggedIn
        get() = sp.accessToken.isNotBlank()

    suspend fun saveUser(user: User) {
        userDAO.save(user)
    }
}