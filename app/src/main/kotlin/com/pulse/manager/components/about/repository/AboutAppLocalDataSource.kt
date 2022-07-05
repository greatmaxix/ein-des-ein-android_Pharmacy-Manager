package com.pulse.manager.components.about.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

class AboutAppLocalDataSource(private val dataStore: DataStore<Preferences>)