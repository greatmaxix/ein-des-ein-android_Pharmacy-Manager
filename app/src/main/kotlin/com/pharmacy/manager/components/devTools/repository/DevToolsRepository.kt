package com.pharmacy.manager.components.devTools.repository

class DevToolsRepository(
    private val rds: DevToolsRemoteDataSource,
    private val lds: DevToolsLocalDataSource
)