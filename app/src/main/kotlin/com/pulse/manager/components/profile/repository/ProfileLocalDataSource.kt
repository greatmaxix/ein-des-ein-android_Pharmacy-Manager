package com.pulse.manager.components.profile.repository

import com.pulse.manager.components.product.model.RecentlyRecommendedDAO
import com.pulse.manager.components.signIn.model.UserDAO
import com.pulse.manager.data.local.SPManager

class ProfileLocalDataSource(private val sp: SPManager, private val userDAO: UserDAO, private val recentlyRecommendedDAO: RecentlyRecommendedDAO) {

    val refreshToken = sp.refreshToken

    fun getUser() = userDAO.get()

    suspend fun logout() {
        sp.clear()
        val user = userDAO.getUser()
        user?.let { userDAO.delete(it) }
        recentlyRecommendedDAO.clear()
    }
}