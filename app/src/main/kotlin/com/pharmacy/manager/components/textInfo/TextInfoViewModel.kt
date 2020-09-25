package com.pharmacy.manager.components.textInfo

import com.pharmacy.manager.components.textInfo.repository.TextInfoRepository
import com.pharmacy.manager.core.base.mvvm.BaseViewModel

class TextInfoViewModel(private val repository: TextInfoRepository, val args: TextInfoFragmentArgs) : BaseViewModel()