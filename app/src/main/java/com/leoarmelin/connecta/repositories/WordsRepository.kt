package com.leoarmelin.connecta.repositories

import com.leoarmelin.connecta.models.GetDailyWordsResponse
import com.leoarmelin.connecta.models.Result
import com.leoarmelin.connecta.retrofit.WordsRetrofitClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class WordsRepository {
    private val wordsService = WordsRetrofitClient.apiService

    suspend fun getDailyWords(): Flow<Result<GetDailyWordsResponse>> = flow {
        emit(Result.Loading)

        try {
            val response = wordsService.getDailyWords()
            emit(Result.Success(response))
        } catch (e: HttpException) {
            emit(Result.Error("HTTP Exception: ${e.message}"))
        } catch (e: Exception) {
            emit(Result.Error("Something went wrong"))
        }
    }
}