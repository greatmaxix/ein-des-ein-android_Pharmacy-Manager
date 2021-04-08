package com.pulse.manager.components.scanner.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.pulse.manager.core.extensions.getOnes
import com.pulse.manager.core.extensions.put
import com.pulse.manager.data.local.Preferences.QrCodeShown.FIELD_QR_CODE_DESCRIPTION_SHOWN

class ScannerLocalDataSource(private val dataStore: DataStore<Preferences>) {

    fun isQrCodeDescriptionShown(): Boolean = dataStore.getOnes(FIELD_QR_CODE_DESCRIPTION_SHOWN, false)

    suspend fun setDescriptionShown() = dataStore.put(FIELD_QR_CODE_DESCRIPTION_SHOWN, true)
}