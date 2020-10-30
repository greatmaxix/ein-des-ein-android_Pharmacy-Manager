package com.pulse.manager.components.splash.repository

import com.pulse.manager.components.signIn.model.User
import com.pulse.manager.components.signIn.model.UserDAO
import com.pulse.manager.data.local.SPManager

class SplashLocalDataSource(private val sp: SPManager, private val userDAO: UserDAO) {

    val isUserLoggedIn
        get() = sp.accessToken.isNotBlank()

    suspend fun saveUser(user: User) = userDAO.save(user)
}