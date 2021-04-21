package com.pulse.manager.components.language

import com.pulse.core.utils.flow.StateEventFlow
import com.pulse.manager.components.language.model.LanguageAdapterModel
import com.pulse.manager.components.language.repository.LanguageRepository
import com.pulse.manager.core.base.mvvm.BaseViewModel
import com.pulse.manager.core.locale.LocaleEnum
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class LanguageViewModel(private val repository: LanguageRepository) : BaseViewModel() {

    private val selectedLocale = repository.appLocale
    val languageState = StateEventFlow(
        LocaleEnum.values().map {
            LanguageAdapterModel(it, selectedLocale == it)
        }
    )

    fun setLanguage(locale: LocaleEnum) = repository.setLocale(locale)
}