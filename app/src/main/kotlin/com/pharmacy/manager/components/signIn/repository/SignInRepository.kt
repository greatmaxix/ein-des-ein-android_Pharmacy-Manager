package com.pharmacy.manager.components.signIn.repository

class SignInRepository(
    private val rds: SignInRemoteDataSource,
    private val lds: SignInLocalDataSource
)