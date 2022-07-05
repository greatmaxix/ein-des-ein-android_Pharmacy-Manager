package com.pulse.manager.components.signIn.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.pulse.manager.components.signIn.model.User
import com.pulse.manager.components.signIn.model.UserDAO
import com.pulse.manager.core.extensions.put
import com.pulse.manager.data.local.Preferences.Token.FIELD_ACCESS_TOKEN
import com.pulse.manager.data.local.Preferences.Token.FIELD_REFRESH_TOKEN

class SignInLocalDataSource(private val dataStore: DataStore<Preferences>, private val userDAO: UserDAO) {

    suspend fun setAccessToken(token: String) = dataStore.put(FIELD_ACCESS_TOKEN, token)

    suspend fun setRefreshToken(token: String) = dataStore.put(FIELD_REFRESH_TOKEN, token)

    suspend fun saveUser(user: User) = userDAO.save(user)
}