package com.leoarmelin.connecta.services

import com.leoarmelin.connecta.models.GetDailyWordsResponse
import retrofit2.http.GET

interface WordsService {
    @GET("dailyWords.json")
    suspend fun getDailyWords(): GetDailyWordsResponse
}