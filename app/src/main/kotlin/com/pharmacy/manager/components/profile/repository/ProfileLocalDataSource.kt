package com.pharmacy.manager.components.profile.repository

import com.pharmacy.manager.components.signIn.model.UserDAO
import com.pharmacy.manager.data.local.SPManager

class ProfileLocalDataSource(private val sp: SPManager, private val userDAO: UserDAO) {

    val refreshToken = sp.refreshToken

    fun getUser() = userDAO.get()

    suspend fun logout() {
        sp.clear()
        val user = userDAO.getUser()
        user?.let { userDAO.delete(it) }
    }
}