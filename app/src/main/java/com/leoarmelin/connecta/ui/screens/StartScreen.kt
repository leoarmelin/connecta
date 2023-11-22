package com.leoarmelin.connecta.ui.screens

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.leoarmelin.connecta.R
import com.leoarmelin.connecta.navigation.Routes.GameScreen
import com.leoarmelin.connecta.navigation.Routes.WinScreen
import com.leoarmelin.connecta.ui.components.AppButton
import com.leoarmelin.connecta.ui.theme.Typography
import com.leoarmelin.connecta.viewmodels.WordsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.material3.MaterialTheme.colorScheme as mtc

@Composable
fun StartScreen(
    navController: NavController = rememberNavController(),
    wordsViewModel: WordsViewModel
) {
    val hasWon by wordsViewModel.hasWon.collectAsState()
    var askToLeave by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    BackHandler(askToLeave) {
        coroutineScope.launch {
            askToLeave = false
            Toast.makeText(context, "Press back again to leave", Toast.LENGTH_SHORT).show()
            delay(2000)
            askToLeave = true
        }
    }

    BackHandler(!askToLeave) {
        coroutineScope.launch {
            (context as? Activity)?.finish()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Icon(
            painter = painterResource(id = R.drawable.home_bg_top),
            contentDescription = "",
            tint = Color.Unspecified,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .aspectRatio(2.287f)
        )

        Icon(
            painter = painterResource(id = R.drawable.home_bg_bottom),
            contentDescription = "",
            tint = Color.Unspecified,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .aspectRatio(1.549f)
        )

        Column(
            modifier = Modifier.fillMaxSize().padding(bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(R.drawable.icon),
                contentDescription = "App Icon",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(120.dp)
                    .scale(2f),
            )

            Text(
                text = "Bem vindo ao\nConnecta!",
                textAlign = TextAlign.Center,
                style = Typography.titleMedium,
                color = mtc.onBackground
            )

            Text(
                text = "Forme 4 grupos de 4 palavras!",
                textAlign = TextAlign.Center,
                style = Typography.bodyMedium,
                color = mtc.onBackground,
                modifier = Modifier.padding(top = 64.dp)
            )

            AppButton(
                text = "JOGAR",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp)
            ) {
                navController.navigate(if (!hasWon) GameScreen else WinScreen)
            }
        }
    }
}