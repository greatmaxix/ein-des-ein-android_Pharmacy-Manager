package com.pharmacy.manager.components.home.repository

import com.pharmacy.manager.components.chatList.model.chat.ChatItemDAO
import com.pharmacy.manager.data.local.SPManager

class HomeLocalDataSource(private val sp: SPManager, private val chatItemDAO: ChatItemDAO) {

     fun getOpenedChatsLiveData() = chatItemDAO.getOpenedChatsLiveData()
}