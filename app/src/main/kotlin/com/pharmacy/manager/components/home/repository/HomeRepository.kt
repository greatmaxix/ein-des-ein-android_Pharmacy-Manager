package com.pharmacy.manager.components.home.repository

class HomeRepository(
    private val rds: HomeRemoteDataSource,
    private val lds: HomeLocalDataSource
)