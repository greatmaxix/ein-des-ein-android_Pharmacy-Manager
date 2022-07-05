package com.pulse.manager.components.textInfo.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

class TextInfoLocalDataSource(private val dataStore: DataStore<Preferences>)