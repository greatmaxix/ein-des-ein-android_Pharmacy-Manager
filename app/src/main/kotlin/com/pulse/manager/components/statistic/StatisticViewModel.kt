package com.pulse.manager.components.statistic

import com.pulse.manager.components.statistic.repository.StatisticRepository
import com.pulse.manager.core.base.mvvm.BaseViewModel
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class StatisticViewModel(private val repository: StatisticRepository) : BaseViewModel()