package com.pulse.manager.components.main.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.pulse.manager.components.chat_list.model.chat.ChatItemDAO
import com.pulse.manager.components.signIn.model.UserDAO
import com.pulse.manager.core.extensions.put
import com.pulse.manager.data.local.Preferences.Chat.FIELD_IS_CHAT_FOREGROUND

class MainLocalDataSource(
    private val dataStore: DataStore<Preferences>,
    private val userDao: UserDAO, // TODO for profile image
    private val chatItemDAO: ChatItemDAO
) {

    fun getUserFlow() = userDao.get()

    suspend fun setChatForeground(isForeground: Boolean) {
        dataStore.put(FIELD_IS_CHAT_FOREGROUND, isForeground)
    }

    suspend fun getChat(chatId: Int) = chatItemDAO.getChat(chatId)
}