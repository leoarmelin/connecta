package com.leoarmelin.connecta.repositories

import com.leoarmelin.connecta.models.Game
import com.leoarmelin.connecta.models.GetDailyWordsResponse
import com.leoarmelin.connecta.models.Result
import com.leoarmelin.connecta.retrofit.WordsRetrofitClient
import com.leoarmelin.connecta.room.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class WordsRepository @Inject constructor(
    roomDatabase: AppDatabase
) {
    private val wordsService = WordsRetrofitClient.apiService
    private val gameDao = roomDatabase.gameDao()

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

    fun getCurrentGame(): Flow<Game?> = gameDao.getCurrentGame()

    fun saveOrUpdateGame(game: Game) = gameDao.saveOrUpdateGame(game)

    fun getAlreadyPlayedCategoriesIds(): List<String> {
        val categories = mutableListOf<String>()

        gameDao.getAlreadyPlayedGames().forEach { game ->
            game.words.forEach { word ->
                if (!categories.contains(word.category)) {
                    categories.add(word.category)
                }
            }
        }

        return categories
    }

    fun getLastPlayedGame() = gameDao.getLastPlayedGame()
}