package com.pulse.manager.core.base.helper

import com.pulse.manager.core.base.fragment.dialog.message.MessageDialogData

interface IUiHelper {

    fun showLoading(show: Boolean)

    fun showMessage(message: String)

    fun showDialog(message: String, block: (MessageDialogData.() -> Unit) = {})
}