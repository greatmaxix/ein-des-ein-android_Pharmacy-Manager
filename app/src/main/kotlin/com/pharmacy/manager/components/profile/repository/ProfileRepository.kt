package com.pharmacy.manager.components.profile.repository

class ProfileRepository(
    private val rds: ProfileRemoteDataSource,
    private val lds: ProfileLocalDataSource
)