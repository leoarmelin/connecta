package com.leoarmelin.connecta.helpers

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("wordsPreferences", Context.MODE_PRIVATE)


    fun saveShouldRenewGame(value: Boolean) {
        sharedPreferences.edit().putBoolean(SHOULD_RENEW_GAME, value).apply()
    }

    fun getShouldRenewGame(): Boolean {
        return sharedPreferences.getBoolean(SHOULD_RENEW_GAME, true)
    }

    companion object {
        const val SHOULD_RENEW_GAME = "shouldRenewGame"
    }
}
