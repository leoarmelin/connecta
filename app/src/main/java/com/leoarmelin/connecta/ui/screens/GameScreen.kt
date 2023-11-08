package com.leoarmelin.connecta.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.leoarmelin.connecta.ui.theme.Typography
import com.leoarmelin.connecta.viewmodels.WordsViewModel

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

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Game Screen",
            style = Typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .clickable {
                    navController.popBackStack()
                }
        )

        Text(text = "Has won: $hasWon")
        Text(text = "Tries: $tries")

        words.forEach { word ->
            val color by animateColorAsState(
                label = "color",
                targetValue = when {
                    finishedWords.contains(word) -> Color.Gray
                    correctWords.contains(word) -> Color.Green
                    wrongWords.contains(word) -> Color.Red
                    selectedWords.contains(word) -> Color.Blue
                    else -> Color.Black
                }
            )
            Text(word.value, color = color, modifier = Modifier.clickable { wordsViewModel.selectWord(word) })
        }
    }
}