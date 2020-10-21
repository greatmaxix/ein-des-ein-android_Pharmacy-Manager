package com.pharmacy.manager.components.chatList

import androidx.paging.ExperimentalPagingApi
import com.pharmacy.manager.components.chatList.repository.ChatListLocalDataSource
import com.pharmacy.manager.components.chatList.repository.ChatListRemoteDataSource
import com.pharmacy.manager.components.chatList.repository.ChatListRepository
import com.pharmacy.manager.data.local.DBManager
import org.koin.androidx.fragment.dsl.fragment
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

    fragment { ChatListFragment(get()) }
}