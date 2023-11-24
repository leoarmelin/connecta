package com.leoarmelin.connecta.room.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class AttemptsConverter {
    private val gson = Gson()
    @TypeConverter
    fun attemptsToJson(attempts: List<String>): String {
        return gson.toJson(attempts)
    }

    @TypeConverter
    fun jsonToAttempts(json: String): List<String> {
        val objectType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(json, objectType) ?: emptyList()
    }
}