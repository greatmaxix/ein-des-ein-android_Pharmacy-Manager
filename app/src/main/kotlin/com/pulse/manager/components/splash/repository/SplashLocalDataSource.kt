package com.pulse.manager.components.splash.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.pulse.manager.components.signIn.model.User
import com.pulse.manager.components.signIn.model.UserDAO
import com.pulse.manager.core.extensions.getOnes
import com.pulse.manager.data.local.Preferences.Token.FIELD_ACCESS_TOKEN

class SplashLocalDataSource(private val dataStore: DataStore<Preferences>, private val userDAO: UserDAO) {

    val isUserLoggedIn
        get() = dataStore.getOnes(FIELD_ACCESS_TOKEN, "").isNotBlank()

    suspend fun saveUser(user: User) = userDAO.save(user)
}