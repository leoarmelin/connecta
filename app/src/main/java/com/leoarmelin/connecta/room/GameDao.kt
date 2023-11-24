package com.leoarmelin.connecta.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.leoarmelin.connecta.models.Game
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Query("SELECT * FROM game WHERE has_won = 0 LIMIT 1")
    fun getCurrentGame(): Flow<Game?>

    @Query("SELECT * FROM game WHERE last_day_played = (SELECT MAX(last_day_played) FROM game) LIMIT 1")
    fun getLastPlayedGame(): Game

    @Query("SELECT * FROM game WHERE has_won = 1")
    fun getAlreadyPlayedGames(): List<Game>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveOrUpdateGame(game: Game)
}