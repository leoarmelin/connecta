package com.leoarmelin.connecta.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.leoarmelin.connecta.helpers.SharedPreferencesHelper
import com.leoarmelin.connecta.repositories.WordsRepository
import com.leoarmelin.connecta.room.AppDatabase
import com.leoarmelin.connecta.room.converters.AttemptsConverter
import com.leoarmelin.connecta.room.converters.WordsConverter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideSharedPreferencesHelper(
        @ApplicationContext context: Context
    ) = SharedPreferencesHelper(context)

    @Provides
    fun provideWordsRepository(
        roomDatabase: AppDatabase
    ) = WordsRepository(roomDatabase)

    @Provides
    fun provideRoomDatabase(
        @ApplicationContext applicationContext: Context
    ): AppDatabase = Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java,
        "app-database"
    )
        .addTypeConverter(WordsConverter())
        .addTypeConverter(AttemptsConverter())
        .build()
}