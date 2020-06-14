package shiverawe.github.com.receipt.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

object Storage {
    private const val PREF_NAME = "pref"
    private const val IS_LOGGED_IN = "is_logged_in"
    private const val USER_NAME = "user_name"
    private const val USER_PHONE = "user_phone"
    private const val USER_EMAIL = "user_email"
    private const val USER_PASSWORD = "user_password"
    private const val USER_TOKEN = "user_token"

    var isLogin: Boolean
        set(value) {
            editStorage {
                putBoolean(IS_LOGGED_IN, value)
            }
        }
        get() = storage?.getBoolean(IS_LOGGED_IN, false) ?: false

    var userName: String
        set(value) {
            editStorage {
                putString(USER_NAME, value)
            }
        }
        get() = storage?.getString(USER_NAME, "").orEmpty()

    var userPhone: String
        set(value) {
            editStorage {
                putString(USER_PHONE, value)
            }
        }
        get() = storage?.getString(USER_PHONE, "").orEmpty()

    var userEmail: String
        set(value) {
            editStorage {
                putString(USER_EMAIL, value)
            }
        }
        get() = storage?.getString(USER_EMAIL, "").orEmpty()

    var userPassword: String
        set(value) {
            editStorage {
                putString(USER_PASSWORD, value)
            }
        }
        get() = storage?.getString(USER_PASSWORD, "").orEmpty()

    var token: String
        set(value) {
            editStorage {
                putString(USER_TOKEN, value)
            }
        }
        get() = storage?.getString(USER_TOKEN, "").orEmpty()

    private var storage: SharedPreferences? = null

    fun attach(context: Context) {
        storage = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE)
    }

    fun detach() {
        storage = null
    }

    fun reset() {
        editStorage { clear() }
    }

    private fun editStorage(action: SharedPreferences.Editor.() -> Unit) {
        val editor = storage?.edit() ?: return
        action(editor)
        editor.apply()
    }
}