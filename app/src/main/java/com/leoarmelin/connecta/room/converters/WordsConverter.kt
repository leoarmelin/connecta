package com.leoarmelin.connecta.room.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.leoarmelin.connecta.models.Word

@ProvidedTypeConverter
class WordsConverter {

    private val gson = Gson()
    @TypeConverter
    fun wordsToJson(words: List<Word>): String {
        return gson.toJson(words)
    }

    @TypeConverter
    fun jsonToWords(json: String): List<Word> {
        val objectType = object : TypeToken<List<Word>>() {}.type
        return gson.fromJson(json, objectType) ?: emptyList()
    }
}