package com.leoarmelin.connecta.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leoarmelin.connecta.helpers.SharedPreferencesHelper
import com.leoarmelin.connecta.models.Result
import com.leoarmelin.connecta.models.Word
import com.leoarmelin.connecta.repositories.WordsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class WordsViewModel(
    private val wordsRepository: WordsRepository = WordsRepository(),
    private val sharedPreferencesHelper: SharedPreferencesHelper
) : ViewModel() {
    private val _lastDayPlayed = MutableStateFlow<LocalDate>(LocalDate.now())

    private val _words = MutableStateFlow<List<Word>>(emptyList())
    val words get() = _words.asStateFlow()

    private val _selectedWords = MutableStateFlow<List<Word>>(emptyList())
    val selectedWords get() = _selectedWords.asStateFlow()

    private val _correctWords = MutableStateFlow<List<Word>>(emptyList())
    val correctWords get() = _correctWords.asStateFlow()

    private val _wrongWords = MutableStateFlow<List<Word>>(emptyList())
    val wrongWords get() = _wrongWords.asStateFlow()

    private val _finishedWords = MutableStateFlow<List<Word>>(emptyList())
    val finishedWords get() = _finishedWords.asStateFlow()

    init {
        initializeDate()
        getDailyWords()

        viewModelScope.launch {
            _selectedWords.collect { selectedWords ->
                // Once we selected the WORDS_PER_CATEGORY amount, follow the logic
                if (selectedWords.size < WORDS_PER_CATEGORY) return@collect

                // If all the words have the same category
                if (selectedWords.distinctBy { it.category }.size == 1) {
                    val newList = _correctWords.value.toMutableList()
                    newList.addAll(selectedWords)
                    _correctWords.value = newList

                    // Change form correctWords to finishedWords after 1s
                    delay(1000)
                    val newListTwo = _finishedWords.value.toMutableList()
                    newListTwo.addAll(selectedWords)
                    _finishedWords.value = newListTwo
                    _correctWords.value = emptyList()
                } else {
                    val newList = _wrongWords.value.toMutableList()
                    newList.addAll(selectedWords)
                    _wrongWords.value = newList

                    // Clear wrong words after 500ms
                    delay(500)
                    _wrongWords.value = emptyList()
                }
            }
        }
    }

    private fun initializeDate() {
        val savedDay = sharedPreferencesHelper.getDay()
        val now = LocalDate.now()
        if (savedDay != now) {
            _lastDayPlayed.value = now
            sharedPreferencesHelper.saveDay(now)
        } else {
            _lastDayPlayed.value = savedDay
        }
    }

    private fun getDailyWords() {
        if (_words.value.isNotEmpty()) return

        viewModelScope.launch(Dispatchers.IO) {
            wordsRepository.getDailyWords().collect { result ->
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        _words.value = sliceListOfWordsBy(result.data.words)
                        println(_words.value)
                    }

                    is Result.Error -> {}
                }
            }
        }
    }

    private fun sliceListOfWordsBy(words: List<List<Word>>): List<Word> =
        words.shuffled().take(CATEGORIES_PER_GAME).flatten().shuffled()

    fun selectWord(word: Word) {
        if (_selectedWords.value.size >= WORDS_PER_CATEGORY) return

        val newList = _selectedWords.value.toMutableList()
        newList.add(word)
        _selectedWords.value = newList
    }

    companion object {
        const val WORDS_PER_CATEGORY = 4
        const val CATEGORIES_PER_GAME = 5
    }
}