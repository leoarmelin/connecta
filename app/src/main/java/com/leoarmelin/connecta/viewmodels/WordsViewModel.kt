package com.leoarmelin.connecta.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leoarmelin.connecta.helpers.SharedPreferencesHelper
import com.leoarmelin.connecta.models.Result
import com.leoarmelin.connecta.models.Word
import com.leoarmelin.connecta.repositories.WordsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class WordsViewModel(
    private val wordsRepository: WordsRepository = WordsRepository(),
    sharedPreferencesHelper: SharedPreferencesHelper
) : ViewModel() {
    private val _lastDayPlayed = MutableStateFlow<LocalDate>(LocalDate.now())

    private val _words = MutableStateFlow<List<List<Word>>>(emptyList())
    val words get() = _words.asStateFlow()

    init {
        val savedDay = sharedPreferencesHelper.getDay()
        val now = LocalDate.now()
        if (savedDay != now) {
            _lastDayPlayed.value = now
            sharedPreferencesHelper.saveDay(now)
        } else {
            _lastDayPlayed.value = savedDay
        }
    }

    fun getDailyWords() {
        viewModelScope.launch(Dispatchers.IO) {
            wordsRepository.getDailyWords().collect { result ->
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        _words.value = sliceListOfWordsBy(result.data.words, 5)
                    }

                    is Result.Error -> {}
                }
            }
        }
    }

    fun sliceListOfWordsBy(words: List<List<Word>>, numberOfCategories: Int): List<List<Word>> =
        words.shuffled().take(numberOfCategories)
}