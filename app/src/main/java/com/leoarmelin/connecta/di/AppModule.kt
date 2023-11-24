package com.leoarmelin.connecta.di

import android.content.Context
import com.leoarmelin.connecta.helpers.SharedPreferencesHelper
import com.leoarmelin.connecta.repositories.WordsRepository
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
    fun provideWordsRepository() = WordsRepository()
}