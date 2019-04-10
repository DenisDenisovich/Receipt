package shiverawe.github.com.receipt.utils

import android.app.Activity
import android.content.Context

object Settings {
    private const val PREF_NAME = "pref"
    private const val PREF_IS_DEVELOP = "is_develop"

    fun setDevelopMod(context: Context, flag: Boolean) {
        context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE).edit().apply {
            putBoolean(PREF_IS_DEVELOP, flag)
            apply()
        }
    }
    fun getDevelopMod(context: Context): Boolean {
        return context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE).getBoolean(PREF_IS_DEVELOP, false)
    }
}