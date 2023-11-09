package com.leoarmelin.connecta.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.leoarmelin.connecta.R
import com.leoarmelin.connecta.navigation.Routes.GameScreen
import com.leoarmelin.connecta.navigation.Routes.WinScreen
import com.leoarmelin.connecta.ui.components.AppButton
import com.leoarmelin.connecta.ui.theme.Typography
import com.leoarmelin.connecta.viewmodels.WordsViewModel
import androidx.compose.material3.MaterialTheme.colorScheme as mtc

@Composable
fun StartScreen(
    navController: NavController = rememberNavController(),
    wordsViewModel: WordsViewModel
) {
    val hasWon by wordsViewModel.hasWon.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(R.drawable.icon),
            contentDescription = "App Icon",
            tint = Color.Unspecified,
            modifier = Modifier
                .size(240.dp)
        )

        Text(
            text = "Connecta",
            style = Typography.titleMedium,
            color = mtc.onBackground
        )

        Text(
            text = "Forme 5 grupos de 4 palavras",
            style = Typography.bodyLarge,
            color = mtc.onBackground
        )

        AppButton(
            text = "JOGAR",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 40.dp)
        ) {
            navController.navigate(if (!hasWon) GameScreen else WinScreen)
        }
    }
}