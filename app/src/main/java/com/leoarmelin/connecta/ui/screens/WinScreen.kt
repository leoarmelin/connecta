package com.leoarmelin.connecta.ui.screens

import android.content.Intent
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
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
    wordsViewModel: WordsViewModel,
    onLoadAd: () -> Unit
) {
    val context = LocalContext.current
    val words by wordsViewModel.words.collectAsState()
    val mistakes by wordsViewModel.mistakes.collectAsState()
    val hasWon by wordsViewModel.hasWon.collectAsState()
    var isAdLoading by remember { mutableStateOf(false) }

    val sections = remember(words) {
        words.map { it.category }.distinct().sorted()
    }

    val winPhrase = remember(mistakes) {
        when(mistakes) {
            0 -> "Você nunca errou! Parabéns!! \uD83E\uDD29"
            1 -> "Você venceu após apenas 1 erro! \uD83D\uDE04"
            in 2..20 -> "O importante é persistir! Você venceu após $mistakes erros! \uD83D\uDC4F"
            else -> "Foi bem difícil, mas você conseguiu após $mistakes erros! Parabéns! \uD83D\uDC4D"
        }
    }
    val sharePhrase = remember(mistakes) {
        when(mistakes) {
            0 -> "Venci hoje sem nenhum erro! Viva o Connecta!! \uD83E\uDD29"
            1 -> "Venci hoje com 1 só erro! Somos todos Connecta!! \uD83D\uDE04"
            in 2..20 -> "O que vale é tentar. Venci hoje com $mistakes erros! Vai Connecta!! \uD83D\uDC4F"
            else -> "Foi bem difícil, mas eu consegui após $mistakes erros! Da próxima vai Connecta!! \uD83D\uDC4D"
        } + "\n\nhttps://play.google.com/store/apps/details?id=com.leoarmelin.connecta"
    }

    BackHandler {
        navController.navigate(Routes.StartScreen)
    }

    LaunchedEffect(hasWon) {
        if (hasWon) return@LaunchedEffect

        navController.navigate(Routes.GameScreen)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = "Você Venceu Hoje!",
                style = Typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .clickable {
                        navController.navigate(Routes.GameScreen)
                    }
            )
        }
        item {
            Text(
                text = winPhrase,
                style = Typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )
        }
        item {
            AppButton(
                text = "JOGAR MAIS",
                isLoading = isAdLoading,
                isEnabled = !isAdLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 40.dp),
                onClick = {
                    isAdLoading = true
                    onLoadAd()
                }
            )
        }
        item {
            AppButton(
                text = "COMPARTILHAR",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, sharePhrase)
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                context.startActivity(shareIntent)
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