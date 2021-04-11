package com.pulse.manager.components.home.repository

import com.pulse.manager.components.chat_list.model.chat.ChatItemDAO

class HomeLocalDataSource(private val chatItemDAO: ChatItemDAO) {

    fun getOpenedChatsFlow() = chatItemDAO.getOpenedChatsFlow()
}