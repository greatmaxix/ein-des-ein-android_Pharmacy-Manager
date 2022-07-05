package com.pulse.manager.components.profile.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.pulse.manager.components.signIn.model.UserDAO
import com.pulse.manager.core.extensions.clearAll
import com.pulse.manager.core.extensions.getOnes
import com.pulse.manager.data.local.Preferences.Token.FIELD_REFRESH_TOKEN

class ProfileLocalDataSource(
    private val dataStore: DataStore<Preferences>,
    private val userDAO: UserDAO
) {

    val refreshToken = dataStore.getOnes(FIELD_REFRESH_TOKEN, "")

    fun getUser() = userDAO.get()

    suspend fun logout() {
        dataStore.clearAll()
        userDAO.clear()
    }
}