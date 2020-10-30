package com.pulse.manager.components.textInfo.repository

class TextInfoRepository(
    private val rds: TextInfoRemoteDataSource,
    private val lds: TextInfoLocalDataSource
)