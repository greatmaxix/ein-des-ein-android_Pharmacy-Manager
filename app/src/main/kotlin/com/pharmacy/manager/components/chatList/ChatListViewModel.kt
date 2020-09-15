package com.pharmacy.manager.components.chatList

import androidx.lifecycle.LiveData
import com.pharmacy.manager.components.chatList.model.TempChat
import com.pharmacy.manager.components.devTools.repository.DevToolsRepository
import com.pharmacy.manager.core.base.mvvm.BaseViewModel
import com.pharmacy.manager.core.general.SingleLiveEvent
import com.pharmacy.manager.data.DummyData

class ChatListViewModel(private val repository: DevToolsRepository) : BaseViewModel() {

    private val _chatListLiveData by lazy { SingleLiveEvent<ArrayList<TempChat>>() }
    val chatListLiveData: LiveData<ArrayList<TempChat>> by lazy { _chatListLiveData }

    init {
        _chatListLiveData.value = DummyData.chatList
    }
}