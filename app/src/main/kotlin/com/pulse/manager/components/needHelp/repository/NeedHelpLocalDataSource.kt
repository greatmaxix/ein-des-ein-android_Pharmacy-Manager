package com.pulse.manager.components.needHelp.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

class NeedHelpLocalDataSource(private val dataStore: DataStore<Preferences>)