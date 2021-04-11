package com.pulse.manager.components.statistic

import com.pulse.manager.components.statistic.repository.StatisticLocalDataSource
import com.pulse.manager.components.statistic.repository.StatisticRemoteDataSource
import com.pulse.manager.components.statistic.repository.StatisticRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val statisticModule = module {

    single { StatisticRepository(get(), get()) }
    single { StatisticLocalDataSource(get()) }
    single { StatisticRemoteDataSource(get()) }

    viewModel { StatisticViewModel(get()) }

    fragment { StatisticFragment() }
}