package com.pulse.manager.components.aboutApp.repository

class AboutAppRepository(
    private val rds: AboutAppRemoteDataSource,
    private val lds: AboutAppLocalDataSource
)