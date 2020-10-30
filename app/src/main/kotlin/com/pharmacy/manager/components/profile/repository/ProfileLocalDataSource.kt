package com.pharmacy.manager.components.profile.repository

import com.pharmacy.manager.components.product.model.RecentlyRecommendedDAO
import com.pharmacy.manager.components.signIn.model.UserDAO
import com.pharmacy.manager.data.local.SPManager

class ProfileLocalDataSource(private val sp: SPManager, private val userDAO: UserDAO, private val recentlyRecommendedDAO: RecentlyRecommendedDAO) {

    val refreshToken = sp.refreshToken

    fun getUser() = userDAO.get()

    fun logout() {
        sp.clear()
        val user = userDAO.clear()
    }
}