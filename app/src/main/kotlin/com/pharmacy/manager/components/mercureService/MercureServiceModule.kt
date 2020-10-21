package com.pharmacy.manager.components.mercureService

import com.here.oksse.OkSse
import com.pharmacy.manager.components.mercureService.repository.MercureLocalDataSource
import com.pharmacy.manager.components.mercureService.repository.MercureRemoteDataSource
import com.pharmacy.manager.components.mercureService.repository.MercureRepository
import com.pharmacy.manager.data.local.DBManager
import okhttp3.OkHttpClient
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

val mercureModule = module {

    single {
        val client = OkHttpClient.Builder()
            .readTimeout(0, TimeUnit.SECONDS)
            .build()
        OkSse(client)
    }

    single {
        MercureLocalDataSource(
            get(),
            get<DBManager>().userDAO,
            get<DBManager>().messageRemoteKeysDAO,
            get<DBManager>().messageDAO,
            get<DBManager>().chatsRemoteKeysDAO,
            get<DBManager>().chatItemDAO
        )
    }
    single { MercureRemoteDataSource(get()) }
    single { MercureRepository(get(), get()) }
}