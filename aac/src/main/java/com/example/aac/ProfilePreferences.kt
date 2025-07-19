package com.example.aac

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color

object ProfilePreferences {
    private const val PREF_NAME = "user_profile"
    private const val KEY_NICKNAME = "nickname"
    private const val KEY_THEME_COLOR = "theme_color"

    fun saveNickname(context: Context, nickname: String) {
        getPrefs(context).edit().putString(KEY_NICKNAME, nickname).apply()
    }

    fun getNickname(context: Context): String? {
        return getPrefs(context).getString(KEY_NICKNAME, "")
    }

    fun saveThemeColor(context: Context, color: Int) {
        getPrefs(context).edit().putInt(KEY_THEME_COLOR, color).apply()
    }

    fun getThemeColor(context: Context): Int {
        return getPrefs(context).getInt(KEY_THEME_COLOR, Color.WHITE)
    }

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }
}
