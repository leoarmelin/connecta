package com.leoarmelin.connecta.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.leoarmelin.connecta.models.Game
import com.leoarmelin.connecta.room.converters.AttemptsConverter
import com.leoarmelin.connecta.room.converters.WordsConverter

@Database(entities = [Game::class], version = 1)
@TypeConverters(WordsConverter::class, AttemptsConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
}