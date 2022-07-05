package com.pulse.manager.components.about.repository

class AboutAppRepository(
    private val rds: AboutAppRemoteDataSource,
    private val lds: AboutAppLocalDataSource
)