package com.example.aac

import android.content.Context
import android.content.SharedPreferences

object UserRepository {

    private const val PREF_NAME = "user_info"
    private const val KEY_EMAIL = "user_email"
    private const val KEY_PASSWORD = "user_password"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveUser(context: Context, email: String, password: String) {
        getPrefs(context).edit().apply {
            putString(KEY_EMAIL, email)
            putString(KEY_PASSWORD, password)
            apply()
        }
    }

    fun isLoginValid(context: Context, email: String, password: String): Boolean {
        val savedEmail = getPrefs(context).getString(KEY_EMAIL, null)
        val savedPassword = getPrefs(context).getString(KEY_PASSWORD, null)
        return savedEmail == email && savedPassword == password
    }

    fun isUserRegistered(context: Context): Boolean {
        return getPrefs(context).contains(KEY_EMAIL) && getPrefs(context).contains(KEY_PASSWORD)
    }

    fun clearUser(context: Context) {
        getPrefs(context).edit().clear().apply()
    }
}
