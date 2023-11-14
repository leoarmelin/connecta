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

    fun saveMistakes(value: Int) {
        sharedPreferences.edit().putInt(MISTAKES, value).apply()
    }

    fun getMistakes(): Int {
        return sharedPreferences.getInt(MISTAKES, 0)
    }

    fun saveHasWon(value: Boolean) {
        sharedPreferences.edit().putBoolean(HAS_WON, value).apply()
    }

    fun getHasWon(): Boolean {
        return sharedPreferences.getBoolean(HAS_WON, false)
    }

    fun saveCategoriesId(categoriesId: Set<String>) {
        val currentSet = getCategoriesId().toMutableSet()
        currentSet.addAll(categoriesId)
        sharedPreferences.edit().putStringSet(CATEGORIES_ID, currentSet).apply()
    }

    fun getCategoriesId(): Set<String> {
        return sharedPreferences.getStringSet(CATEGORIES_ID, emptySet()) ?: emptySet()
    }

    fun saveCurrentCategoriesId(value: Set<String>) {
        sharedPreferences.edit().putStringSet(CURRENT_CATEGORIES_ID, value).apply()
    }

    fun getCurrentCategoriesId(): Set<String> {
        return sharedPreferences.getStringSet(CURRENT_CATEGORIES_ID, emptySet()) ?: emptySet()
    }

    companion object {
        const val DAY = "day"
        const val MISTAKES = "mistakes"
        const val HAS_WON = "hasWon"
        const val CATEGORIES_ID = "categoriesId"
        const val CURRENT_CATEGORIES_ID = "categoriesId"
    }
}
