package com.leoarmelin.connecta.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leoarmelin.connecta.helpers.SharedPreferencesHelper
import com.leoarmelin.connecta.models.Category
import com.leoarmelin.connecta.models.Game
import com.leoarmelin.connecta.models.Result
import com.leoarmelin.connecta.models.Word
import com.leoarmelin.connecta.repositories.WordsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordsViewModel @Inject constructor(
    private val wordsRepository: WordsRepository,
    private val sharedPreferencesHelper: SharedPreferencesHelper
) : ViewModel() {

    private val _game = MutableStateFlow(Game.startNewGame(emptyList()))

    private val _selectedWords = MutableStateFlow<List<Word>>(emptyList())
    val selectedWords get() = _selectedWords.asStateFlow()

    private val _correctWords = MutableStateFlow<List<Word>>(emptyList())
    val correctWords get() = _correctWords.asStateFlow()

    private val _wrongWords = MutableStateFlow<List<Word>>(emptyList())
    val wrongWords get() = _wrongWords.asStateFlow()

    /** UI VARIABLES **/

    val finishedWords = _game.map { game ->
        println("Aoba - ${game.finishedWords}")
        println()
        return@map game.finishedWords
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val words = _game.map { game ->
        return@map game.words
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val hasWon = _game.map { game ->
        return@map game.hasWon
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        false
    )

    val mistakes = _game.map { game ->
        game.attempts.count { it == null }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        0
    )

    init {
        viewModelScope.launch(Dispatchers.IO) {
            wordsRepository.getCurrentGame().collect { currentGame ->
                val shouldRenewGame = sharedPreferencesHelper.getShouldRenewGame()

                if (currentGame == null && shouldRenewGame) {
                    getDailyWords()
                    return@collect
                } else if (currentGame != null) {
                    _game.value = currentGame
                } else {
                    _game.value = wordsRepository.getLastPlayedGame()
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

                    addCategoryToAttempt(newList.first().category)

                    // Change form correctWords to finishedWords after 1s
                    delay(1000)
                    val newListTwo = _game.value.finishedWords.toMutableList()
                    newListTwo.addAll(selectedWords)
                    saveOrUpdateGame(
                        _game.value.copy(
                            finishedWords = newListTwo
                        )
                    )
                    _correctWords.value = emptyList()
                } else {
                    val newList = _wrongWords.value.toMutableList()
                    newList.addAll(selectedWords)
                    _wrongWords.value = newList

                    addMistakeToAttempt()

                    // Clear wrong words after 500ms
                    delay(500)
                    _wrongWords.value = emptyList()
                }
                _selectedWords.value = emptyList()
            }
        }

        viewModelScope.launch {
            finishedWords.collect { finishedWords ->
                if (finishedWords.isEmpty()) return@collect
                updateHasWonValue(
                    finishedWords.size == _game.value.words.size
                )
            }
        }
    }

    private fun getDailyWords() {
        viewModelScope.launch(Dispatchers.IO) {
            wordsRepository.getDailyWords().collect { result ->
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        val alreadyPlayedIds = wordsRepository.getAlreadyPlayedCategoriesIds()

                        val words = mutableListOf<Word>()

                        val chosenCategories: List<Category> =
                            result.data.categories.filter { !alreadyPlayedIds.contains(it.id) }
                                .shuffled()
                                .take(WORDS_PER_CATEGORY)

                        chosenCategories.forEach { category ->
                            category.words.forEach { word ->
                                words.add(
                                    Word(
                                        value = word,
                                        category = category.name
                                    )
                                )
                            }
                        }

                        saveOrUpdateGame(
                            _game.value.copy(
                                words = words.shuffled()
                            )
                        )
                    }

                    is Result.Error -> {
                        Log.d("Aoba", result.exception)
                    }
                }
            }
        }
    }

    private fun addMistakeToAttempt() {
        val newAttempts = _game.value.attempts.toMutableList()
        newAttempts.add(null)
        saveOrUpdateGame(
            _game.value.copy(
                attempts = newAttempts
            )
        )
    }

    private fun addCategoryToAttempt(category: String) {
        val newAttempts = _game.value.attempts.toMutableList()
        newAttempts.add(category)
        saveOrUpdateGame(
            _game.value.copy(
                attempts = newAttempts
            )
        )
    }

    private fun updateHasWonValue(value: Boolean) {
        if (value) {
            sharedPreferencesHelper.saveShouldRenewGame(false)
        }

        saveOrUpdateGame(
            _game.value.copy(
                hasWon = value
            )
        )
    }

    fun selectWord(word: Word) {
        val selectedWordsCountIsMax = _selectedWords.value.size >= WORDS_PER_CATEGORY
        val isShowingCorrectAnswers = _correctWords.value.isNotEmpty()
        val isShowingWrongAnswers = _wrongWords.value.isNotEmpty()
        val isAlreadyFinished = _game.value.finishedWords.contains(word)

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

    private fun saveOrUpdateGame(game: Game) {
        viewModelScope.launch(Dispatchers.IO) {
            wordsRepository.saveOrUpdateGame(game)
        }
    }

    fun hasSeenAds() {
        sharedPreferencesHelper.saveShouldRenewGame(true)
        getDailyWords()
    }

    companion object {
        const val WORDS_PER_CATEGORY = 4
    }
}