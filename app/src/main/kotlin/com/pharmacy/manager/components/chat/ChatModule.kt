package com.pharmacy.manager.components.chat

import com.pharmacy.manager.components.chat.dialog.SendBottomSheetDialogFragment
import com.pharmacy.manager.components.chat.repository.ChatLocalDataSource
import com.pharmacy.manager.components.chat.repository.ChatRemoteDataSource
import com.pharmacy.manager.components.chat.repository.ChatRepository
import com.pharmacy.manager.data.local.DBManager
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val chatModule = module {

    single { ChatLocalDataSource(get(), get<DBManager>().userDAO, get<DBManager>().remoteKeysDAO, get<DBManager>().messageDAO) }
    single { ChatRemoteDataSource(get()) }
    single { ChatRepository(get(), get()) }

    viewModel { (args: ChatFragmentArgs) -> ChatViewModel(androidApplication(), get(), args) }

    fragment { ChatFragment() }

    fragment { SendBottomSheetDialogFragment() }
}