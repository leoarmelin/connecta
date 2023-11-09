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

    fun saveTries(value: Int) {
        sharedPreferences.edit().putInt(TRIES, value).apply()
    }

    fun getTries(): Int {
        return sharedPreferences.getInt(TRIES, 0)
    }

    companion object {
        const val DAY = "day"
        const val TRIES = "tries"
    }
}
