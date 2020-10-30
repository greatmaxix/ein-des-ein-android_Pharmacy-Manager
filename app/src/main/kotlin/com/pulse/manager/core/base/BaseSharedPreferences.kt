package com.pulse.manager.core.base

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.pulse.manager.BuildConfig

abstract class BaseSharedPreferences(private val context: Context) {

    val sp: SharedPreferences =
        context.getSharedPreferences("${BuildConfig.APPLICATION_ID}_prefs", Context.MODE_PRIVATE)

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> get(key: String, defaultValue: T) = sp.run {
        when (T::class) {
            Boolean::class -> getBoolean(key, defaultValue as Boolean) as T
            Float::class -> getFloat(key, defaultValue as Float) as T
            Int::class -> getInt(key, defaultValue as Int) as T
            Long::class -> getLong(key, defaultValue as Long) as T
            String::class -> getString(key, defaultValue as String) as T
            else -> if (defaultValue is Set<*>) getStringSet(
                key,
                defaultValue as Set<String>
            ) as T else defaultValue
        }
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> get(key: String) = sp.run {
        if (contains(key)) {
            when (T::class) {
                Boolean::class -> getBoolean(key, false) as T
                Float::class -> getFloat(key, 0f) as T
                Int::class -> getInt(key, 0) as T
                Long::class -> getLong(key, 0L) as T
                String::class -> getString(key, null) as T
                else -> getStringSet(key, null) as T
            }
        } else {
            null
        }
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> put(key: String, value: T) = sp.edit {
        when (T::class) {
            Boolean::class -> putBoolean(key, value as Boolean)
            Float::class -> putFloat(key, value as Float)
            Int::class -> putInt(key, value as Int)
            Long::class -> putLong(key, value as Long)
            String::class -> putString(key, value as String)
            else -> {
                if (value is Set<*>) {
                    putStringSet(key, value as Set<String>)
                }
            }
        }
    }

    inline fun <reified T> get(key: Enum<*>, defValue: T) = get(key.name, defValue)

    inline fun <reified T> get(key: Enum<*>): T? = get(key.name)

    inline fun <reified T> put(key: Enum<*>, value: T) = put(key.name, value)

    fun clearAll() = sp.edit {
        sp.all.forEach {
            remove(it.key)
        }
    }
}
