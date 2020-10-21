package com.pharmacy.manager.components.main.repository

import com.pharmacy.manager.components.chatList.model.chat.ChatItemDAO
import com.pharmacy.manager.components.signIn.model.UserDAO
import com.pharmacy.manager.data.local.SPManager

class MainLocalDataSource(
    private val sp: SPManager,
    private val userDao: UserDAO, // TODO for profile image
    private val chatItemDAO: ChatItemDAO
) {

    fun setChatForeground(isForeground: Boolean) {
        sp.isChatForeground = isForeground
    }

    suspend fun getChat(chatId: Int) = chatItemDAO.getChat(chatId)
}