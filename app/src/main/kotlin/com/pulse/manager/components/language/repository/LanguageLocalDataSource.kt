package com.pulse.manager.components.language.repository

import com.pulse.manager.core.locale.ILocaleManager
import com.pulse.manager.core.locale.LocaleEnum

class LanguageLocalDataSource(private val localeManager: ILocaleManager) {

    fun setLocale(locale: LocaleEnum) {
        localeManager.appLocale = locale
    }

    val appLocale
        get() = localeManager.appLocale
}