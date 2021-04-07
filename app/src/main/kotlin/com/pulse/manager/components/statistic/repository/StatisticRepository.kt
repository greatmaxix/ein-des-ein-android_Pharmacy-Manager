package com.pulse.manager.components.statistic.repository

class StatisticRepository(
    private val rds: StatisticRemoteDataSource,
    private val lds: StatisticLocalDataSource
)