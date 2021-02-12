package com.pulse.manager.components.main.repository

import com.pulse.manager.components.chat_list.model.chat.ChatItemDAO
import com.pulse.manager.components.signIn.model.UserDAO
import com.pulse.manager.data.local.SPManager

class MainLocalDataSource(
    private val sp: SPManager,
    private val userDao: UserDAO, // TODO for profile image
    private val chatItemDAO: ChatItemDAO
) {

    fun getUserLiveData() = userDao.get()

    fun setChatForeground(isForeground: Boolean) {
        sp.isChatForeground = isForeground
    }

    suspend fun getChat(chatId: Int) = chatItemDAO.getChat(chatId)
}