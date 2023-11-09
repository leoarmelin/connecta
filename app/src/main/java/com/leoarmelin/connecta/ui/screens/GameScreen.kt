package com.leoarmelin.connecta.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.leoarmelin.connecta.ui.components.FinishedSection
import com.leoarmelin.connecta.ui.components.WordPill
import com.leoarmelin.connecta.ui.components.WordPillState
import com.leoarmelin.connecta.ui.theme.Typography
import com.leoarmelin.connecta.viewmodels.WordsViewModel
import androidx.compose.material3.MaterialTheme.colorScheme as mtc

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GameScreen(
    navController: NavController = rememberNavController(),
    wordsViewModel: WordsViewModel
) {
    val words by wordsViewModel.words.collectAsState()
    val selectedWords by wordsViewModel.selectedWords.collectAsState()
    val correctWords by wordsViewModel.correctWords.collectAsState()
    val wrongWords by wordsViewModel.wrongWords.collectAsState()
    val finishedWords by wordsViewModel.finishedWords.collectAsState()
    val hasWon by wordsViewModel.hasWon.collectAsState()
    val tries by wordsViewModel.tries.collectAsState()
    val sections = remember(finishedWords) {
        words.map { it.category }.distinct().sorted()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        item {
            Text(
                text = "Game Screen",
                style = Typography.titleMedium,
                color = mtc.onBackground,
                modifier = Modifier
                    .clickable {
                        navController.popBackStack()
                    }
            )
        }

        item {
            Text(
                text = "Has won: $hasWon",
                style = Typography.bodyLarge,
                color = mtc.onBackground,
            )
        }

        item {
            Text(
                text = "Tries: $tries",
                style = Typography.bodyLarge,
                color = mtc.onBackground,
            )
        }

        item {
            FlowRow(Modifier.padding(top = 16.dp)) {
                words.forEach { word ->
                    WordPill(
                        wordValue = word.value,
                        state = when {
                            finishedWords.contains(word) -> WordPillState.FINISHED
                            correctWords.contains(word) -> WordPillState.RIGHT
                            wrongWords.contains(word) -> WordPillState.WRONG
                            selectedWords.contains(word) -> WordPillState.SELECTED
                            else -> WordPillState.NORMAL
                        },
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
                    ) {
                        wordsViewModel.selectWord(word)
                    }
                }
            }
        }

        FinishedSection(finishedWords = finishedWords, sections = sections, lazyListScope = this)
    }
}