package com.pulse.manager.components.chat

import com.pulse.manager.components.chat.dialog.SendBottomSheetDialogFragment
import com.pulse.manager.components.chat.repository.ChatLocalDataSource
import com.pulse.manager.components.chat.repository.ChatRemoteDataSource
import com.pulse.manager.components.chat.repository.ChatRepository
import com.pulse.manager.components.chat_list.model.chat.ChatItem
import com.pulse.manager.data.local.DBManager
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@FlowPreview
@OptIn(KoinApiExtension::class)
val chatModule = module {

    single { ChatLocalDataSource(get(), get<DBManager>().userDAO, get<DBManager>().messageRemoteKeysDAO, get<DBManager>().messageDAO, get<DBManager>().chatItemDAO) }
    single { ChatRemoteDataSource(get()) }
    single { ChatRepository(get(), get()) }

    viewModel { (chat: ChatItem) -> ChatViewModel(androidApplication(), get(), chat) }

    fragment { ChatFragment() }

    fragment { SendBottomSheetDialogFragment() }
}