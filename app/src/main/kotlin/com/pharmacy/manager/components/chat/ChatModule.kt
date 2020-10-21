package com.pharmacy.manager.components.chat

import com.pharmacy.manager.components.chat.dialog.SendBottomSheetDialogFragment
import com.pharmacy.manager.components.chat.repository.ChatLocalDataSource
import com.pharmacy.manager.components.chat.repository.ChatRemoteDataSource
import com.pharmacy.manager.components.chat.repository.ChatRepository
import com.pharmacy.manager.components.chatList.model.chat.ChatItem
import com.pharmacy.manager.data.local.DBManager
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@FlowPreview
@OptIn(KoinApiExtension::class)
val chatModule = module {

    single { ChatLocalDataSource(get(), get<DBManager>().userDAO, get<DBManager>().messageRemoteKeysDAO, get<DBManager>().messageDAO) }
    single { ChatRemoteDataSource(get()) }
    single { ChatRepository(get(), get()) }

    viewModel { (chat: ChatItem) -> ChatViewModel(androidApplication(), get(), chat) }

    fragment { ChatFragment() }

    fragment { SendBottomSheetDialogFragment() }
}