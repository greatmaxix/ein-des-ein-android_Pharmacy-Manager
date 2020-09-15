package com.pharmacy.manager.data.local

import android.content.Context
import com.pharmacy.manager.core.base.BaseSharedPreferences
import com.pharmacy.manager.core.general.interfaces.ManagerInterface

class SPManager(context: Context) : BaseSharedPreferences(context), ManagerInterface {

    var pushToken: String
        get() = get(Keys.PUSH_TOKEN, "")
        set(value) = put(Keys.PUSH_TOKEN, value)

    var accessToken: String
        get() = get(Keys.ACCESS_TOKEN, "")
        set(value) = put(Keys.ACCESS_TOKEN, value)

    var email: String
        get() = get(Keys.EMAIL, "")
        set(value) = put(Keys.EMAIL, value)

    override fun clear() {
        clearAll()
    }

    private enum class Keys {
        PUSH_TOKEN,
        EMAIL,
        ACCESS_TOKEN
    }
}