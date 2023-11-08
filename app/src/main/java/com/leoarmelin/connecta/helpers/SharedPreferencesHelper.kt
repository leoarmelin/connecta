package com.leoarmelin.connecta.helpers

import android.content.Context
import android.content.SharedPreferences
import java.time.LocalDate

class SharedPreferencesHelper(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("wordsPreferences", Context.MODE_PRIVATE)

    fun saveDay(value: LocalDate) {
        sharedPreferences.edit().putString(DAY, value.toEpochDay().toString()).apply()
    }

    fun getDay(): LocalDate {
        val epochDay = sharedPreferences.getString(DAY, "0") ?: "0"
        return LocalDate.ofEpochDay(epochDay.toLong())
    }

    companion object {
        const val DAY = "day"
    }
}
