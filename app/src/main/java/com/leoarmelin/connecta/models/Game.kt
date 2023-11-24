package com.leoarmelin.connecta.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID

@Entity
@JsonClass(generateAdapter = true)
data class Game(
    @PrimaryKey
    val id: UUID,
    @ColumnInfo(name = "finished_words")
    val finishedWords: List<Word>,
    @ColumnInfo(name = "has_won")
    val hasWon: Boolean,
    @ColumnInfo(name = "last_day_played")
    val lastDayPlayed: Long,
    @ColumnInfo(name = "attempts")
    val attempts: List<String?>,
    @ColumnInfo(name = "words")
    val words: List<Word>,
) {
    companion object {
        fun startNewGame(words: List<Word>): Game = Game(
            id = UUID.randomUUID(),
            finishedWords = emptyList(),
            hasWon = false,
            lastDayPlayed = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
            attempts = emptyList(),
            words = words
        )
    }
}
