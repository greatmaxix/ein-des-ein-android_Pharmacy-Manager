package com.pulse.manager.components.language.model

import com.pulse.manager.core.locale.LocaleEnum

data class LanguageAdapterModel(
    val language: LocaleEnum,
    var isSelected: Boolean
)