package com.leoarmelin.connecta.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leoarmelin.connecta.helpers.SharedPreferencesHelper
import com.leoarmelin.connecta.models.Category
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
    private val _lastDayPlayed = MutableStateFlow<LocalDate?>(null)

    private val _words = MutableStateFlow<List<Word>>(emptyList())
    val words get() = _words.asStateFlow()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    // val categories get() = _categories.asStateFlow()

    private val _selectedWords = MutableStateFlow<List<Word>>(emptyList())
    val selectedWords get() = _selectedWords.asStateFlow()

    private val _correctWords = MutableStateFlow<List<Word>>(emptyList())
    val correctWords get() = _correctWords.asStateFlow()

    private val _wrongWords = MutableStateFlow<List<Word>>(emptyList())
    val wrongWords get() = _wrongWords.asStateFlow()

    private val _finishedWords = MutableStateFlow<List<Word>>(emptyList())
    val finishedWords get() = _finishedWords.asStateFlow()

    private val _mistakes = MutableStateFlow(0)
    val mistakes get() = _mistakes.asStateFlow()

    private val _hasWon = MutableStateFlow(false)
    val hasWon get() = _hasWon.asStateFlow()

    init {
        initializeGame()

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

                    addOneMoreMistake()

                    // Clear wrong words after 500ms
                    delay(500)
                    _wrongWords.value = emptyList()
                }
                _selectedWords.value = emptyList()
            }
        }

        viewModelScope.launch {
            _finishedWords.collect { finishedWords ->
                if (finishedWords.isEmpty()) return@collect
                updateHasWonValue(
                    _categories.value.map { it.id }.toSet(),
                    finishedWords.size == _words.value.size
                )
            }
        }
    }

    private fun initializeGame() {
        val savedDay = sharedPreferencesHelper.getDay()
        val currentCategoriesIds = sharedPreferencesHelper.getCurrentCategoriesId()
        val now = LocalDate.now()

        if (currentCategoriesIds.isNotEmpty()) {
            _lastDayPlayed.value = now
            sharedPreferencesHelper.saveDay(now)
            sharedPreferencesHelper.saveMistakes(0)
            sharedPreferencesHelper.saveHasWon(false)
        } else {
            _lastDayPlayed.value = savedDay
            _mistakes.value = sharedPreferencesHelper.getMistakes()
            _hasWon.value = sharedPreferencesHelper.getHasWon()
        }

        getDailyWords(currentCategoriesIds.isEmpty())
    }

    fun getDailyWords(renewWords: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            wordsRepository.getDailyWords().collect { result ->
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        val alreadyPlayedIds = sharedPreferencesHelper.getCategoriesId()
                        val currentCategoriesIds = sharedPreferencesHelper.getCurrentCategoriesId()

                        val words = mutableListOf<Word>()
                        val chosenCategories: List<Category>

                        if (renewWords) {
                            chosenCategories =
                                result.data.categories.filter { !alreadyPlayedIds.contains(it.id) }
                                    .shuffled()
                                    .take(WORDS_PER_CATEGORY)

                            sharedPreferencesHelper.saveCurrentCategoriesId(
                                chosenCategories.map { it.id }.toSet()
                            )
                        } else {
                            chosenCategories =
                                result.data.categories.filter { currentCategoriesIds.contains(it.id) }
                        }

                        chosenCategories.take(1).forEach { category ->
                            category.words.forEach { word ->
                                words.add(
                                    Word(
                                        value = word,
                                        category = category.name
                                    )
                                )
                            }
                        }

                        _categories.value = chosenCategories
                        _words.value = words.shuffled()
                    }

                    is Result.Error -> {
                        Log.d("Aoba", result.exception)
                    }
                }
            }
        }
    }

    private fun addOneMoreMistake() {
        _mistakes.value += 1
        sharedPreferencesHelper.saveMistakes(_mistakes.value)
    }

    fun updateHasWonValue(finishedWordsIds: Set<String>, value: Boolean) {
        _hasWon.value = value
        sharedPreferencesHelper.saveHasWon(value)
        sharedPreferencesHelper.saveCurrentCategoriesId(emptySet())
        sharedPreferencesHelper.saveCategoriesId(finishedWordsIds)

        if (value) {
            viewModelScope.launch {
                delay(1500)
                _finishedWords.value = emptyList()
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
    }
}