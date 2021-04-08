package com.pulse.manager.data.local

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object Preferences {

    object Token {
        private const val TOKEN = "token"
        val FIELD_ACCESS_TOKEN = stringPreferencesKey(TOKEN)

        private const val REFRESH_TOKEN = "refresh_token"
        val FIELD_REFRESH_TOKEN = stringPreferencesKey(REFRESH_TOKEN)
    }

    object Chat {
        private const val IS_CHAT_FOREGROUND = "is_chat_foreground"
        val FIELD_IS_CHAT_FOREGROUND = booleanPreferencesKey(IS_CHAT_FOREGROUND)
    }

    object QrCodeShown {
        private const val QR_CODE_DESCRIPTION_SHOWN = "qr_code_description_shown"
        val FIELD_QR_CODE_DESCRIPTION_SHOWN = booleanPreferencesKey(QR_CODE_DESCRIPTION_SHOWN)
    }

    object Email {
        private const val EMAIL = "email"
        val FIELD_EMAIL = stringPreferencesKey(EMAIL)
    }
}