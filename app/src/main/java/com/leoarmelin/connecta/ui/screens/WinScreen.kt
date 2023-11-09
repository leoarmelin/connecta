package com.leoarmelin.connecta.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.leoarmelin.connecta.navigation.Routes
import com.leoarmelin.connecta.ui.components.AppButton
import com.leoarmelin.connecta.ui.components.FinishedSection
import com.leoarmelin.connecta.ui.theme.Typography
import com.leoarmelin.connecta.viewmodels.WordsViewModel

@Composable
fun WinScreen(
    navController: NavController = rememberNavController(),
    wordsViewModel: WordsViewModel
) {
    val words by wordsViewModel.words.collectAsState()
    val mistakes by wordsViewModel.mistakes.collectAsState()

    val sections = remember(words) {
        words.map { it.category }.distinct().sorted()
    }

    BackHandler {
        navController.navigate(Routes.StartScreen)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {

        }
        item {
            Text(
                text = "Você Venceu Hoje!",
                style = Typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .clickable {
                        navController.navigate(Routes.GameScreen)
                    }
            )
        }
        item {
            Text(
                text = "Você venceu após $mistakes erros.",
                style = Typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
        item {
            AppButton(
                text = "COMPARTILHAR",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 40.dp)
            ) {
                //TODO: SHARE
            }
        }

        FinishedSection(
            finishedWords = words,
            sections = sections,
            lazyListScope = this,
            wordsVisibilityAnimSpec = tween(200)
        )
    }
}