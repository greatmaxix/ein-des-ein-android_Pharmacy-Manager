package com.pulse.manager.components.statistic.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

class StatisticLocalDataSource(private val dataStore: DataStore<Preferences>)