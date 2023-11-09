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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class WordsViewModel(
    private val wordsRepository: WordsRepository = WordsRepository(),
    private val sharedPreferencesHelper: SharedPreferencesHelper
) : ViewModel() {
    private val _lastDayPlayed = MutableStateFlow<LocalDate?>(null)

    private val _hasPlayedToday = _lastDayPlayed.map { lastDayPlayed ->
        lastDayPlayed == LocalDate.now()
    }
    val hasPlayedToday get() = _hasPlayedToday

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

    private val _tries = MutableStateFlow(0)
    val tries get() = _tries.asStateFlow()

    val hasWon = _finishedWords.map { finishedWords ->
        finishedWords.size == _words.value.size
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false,
    )

    init {
        initializeDate()

        viewModelScope.launch {
            _lastDayPlayed.collect { lastDayPlayed ->
                if (lastDayPlayed == null) return@collect
                withContext(Dispatchers.IO) {
                    getDailyWords(lastDayPlayed)
                }
            }
        }

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
                _tries.value += 1
                _selectedWords.value = emptyList()
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

    private suspend fun getDailyWords(lastDayPlayed: LocalDate) {
        if (_words.value.isNotEmpty()) return

        wordsRepository.getDailyWords().collect { result ->
            when (result) {
                is Result.Loading -> {}
                is Result.Success -> {
                    val categoriesAmount = result.data.words.size
                    val startIndex =
                        (lastDayPlayed.toEpochDay() % (categoriesAmount - CATEGORIES_PER_GAME)).toInt()
                    val endIndex = startIndex + CATEGORIES_PER_GAME
                    _words.value =
                        result.data.words.subList(startIndex, endIndex).flatten().shuffled()
                }

                is Result.Error -> {}
            }
        }
    }

    fun selectWord(word: Word) {
        val selectedWordsCountIsMax = _selectedWords.value.size >= WORDS_PER_CATEGORY
        val isShowingCorrectAnswers = _correctWords.value.isNotEmpty()
        val isShowingWrongAnswers = _wrongWords.value.isNotEmpty()
        val isAlreadyFinished = _finishedWords.value.contains(word)

        if (selectedWordsCountIsMax ||
            isShowingCorrectAnswers ||
            isShowingWrongAnswers ||
            isAlreadyFinished
        ) return

        val newList = _selectedWords.value.toMutableList()

        if (_selectedWords.value.contains(word)) {
            newList.remove(word)
        } else {
            newList.add(word)
        }
        _selectedWords.value = newList
    }

    companion object {
        const val WORDS_PER_CATEGORY = 4
        const val CATEGORIES_PER_GAME = 2
    }
}