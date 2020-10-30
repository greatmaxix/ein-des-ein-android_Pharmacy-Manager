package com.pulse.manager.components.home.repository

import com.pulse.manager.components.chatList.model.chat.ChatItemDAO
import com.pulse.manager.data.local.SPManager

class HomeLocalDataSource(private val sp: SPManager, private val chatItemDAO: ChatItemDAO) {

    fun getOpenedChatsLiveData() = chatItemDAO.getOpenedChatsLiveData()
}