package com.leoarmelin.connecta.retrofit

import com.leoarmelin.connecta.services.WordsService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object WordsRetrofitClient {
    const val baseUrl = "https://raw.githubusercontent.com/leoarmelin/connecta/main/app/src/main/java/com/leoarmelin/connecta/api/"
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val apiService = retrofit.create(WordsService::class.java)
}