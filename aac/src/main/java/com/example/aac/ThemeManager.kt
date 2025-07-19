package com.example.aac

import android.content.Context
import android.graphics.Color

object ThemeManager {

    fun getThemeColor(context: Context): Int {
        val prefs = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        val hex = prefs.getString("theme_color", "#FF9800")

        return try {
            Color.parseColor(hex)
        } catch (e: IllegalArgumentException) {
            Color.parseColor("#FF9800")
        }
    }
}
