package com.pulse.manager.components.chat_list

import androidx.paging.ExperimentalPagingApi
import com.pulse.manager.components.chat_list.repository.ChatListLocalDataSource
import com.pulse.manager.components.chat_list.repository.ChatListRemoteDataSource
import com.pulse.manager.components.chat_list.repository.ChatListRepository
import com.pulse.manager.data.local.DBManager
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@ExperimentalPagingApi
@OptIn(KoinApiExtension::class)
val chatListModule = module {

    single { ChatListLocalDataSource(get<DBManager>().chatsRemoteKeysDAO, get<DBManager>().chatItemDAO) }
    single { ChatListRemoteDataSource(get()) }
    single { ChatListRepository(get(), get()) }

    viewModel { ChatListViewModel(get()) }
}