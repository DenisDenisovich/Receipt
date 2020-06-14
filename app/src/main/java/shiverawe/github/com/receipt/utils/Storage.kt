package shiverawe.github.com.receipt.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

private const val PREF_NAME = "pref"
private const val IS_LOGGED_IN = "is_logged_in"
private const val USER_NAME = "user_name"
private const val USER_PHONE = "user_phone"
private const val USER_EMAIL = "user_email"
private const val USER_PASSWORD = "user_password"
private const val USER_TOKEN = "user_token"

class Storage(context: Context) {

    private val storage: SharedPreferences = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE)

    var isLogin: Boolean by BooleanPreference(storage, IS_LOGGED_IN, false)
    var userName: String by StringPreference(storage, USER_NAME, "")
    var userPhone: String by StringPreference(storage, USER_PHONE, "")
    var userEmail: String by StringPreference(storage, USER_EMAIL, "")
    var userPassword: String by StringPreference(storage, USER_PASSWORD, "")
    var token: String by StringPreference(storage, USER_TOKEN, "")

    fun reset() {
        editStorage { clear() }
    }

    private fun editStorage(action: SharedPreferences.Editor.() -> Unit) {
        val editor = storage.edit()
        action(editor)
        editor.apply()
    }

    private inner class BooleanPreference(
        val sharedPreferences: SharedPreferences,
        val key: String,
        val defValue: Boolean
    ) : ReadWriteProperty<Any, Boolean> {

        override fun getValue(thisRef: Any, property: KProperty<*>): Boolean =
            sharedPreferences.getBoolean(key, defValue)

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
            editStorage { putBoolean(key, value) }
        }
    }

    private inner class StringPreference(
        val sharedPreferences: SharedPreferences,
        val key: String,
        val defValue: String
    ) : ReadWriteProperty<Any, String> {

        override fun getValue(thisRef: Any, property: KProperty<*>): String =
            sharedPreferences.getString(key, defValue) ?: defValue

        override fun setValue(thisRef: Any, property: KProperty<*>, value: String) {
            editStorage { putString(key, value) }
        }
    }
}