package com.pharmacy.manager.components.signIn.repository

import com.pharmacy.manager.components.signIn.model.User
import com.pharmacy.manager.components.signIn.model.UserDAO
import com.pharmacy.manager.data.local.SPManager

class SignInLocalDataSource(private val sp: SPManager, private val userDAO: UserDAO) {

    fun setAccessToken(token: String) {
        sp.accessToken = token
    }

    fun setRefreshToken(token: String) {
        sp.refreshToken = token
    }

    suspend fun saveUser(user: User) = userDAO.save(user)
}