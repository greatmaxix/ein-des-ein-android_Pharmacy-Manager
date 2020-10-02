package com.pharmacy.manager.components.chatList

import com.pharmacy.manager.components.chatList.repository.ChatListRemoteDataSource
import com.pharmacy.manager.components.chatList.repository.ChatListRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val chatListModule = module {

    single { ChatListRepository(get()) }
    single { ChatListRemoteDataSource(get()) }

    viewModel { ChatListViewModel(get()) }

    fragment { ChatListFragment(get()) }
}