package com.leoarmelin.connecta.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leoarmelin.connecta.navigation.Routes.GameScreen
import com.leoarmelin.connecta.navigation.Routes.StartScreen
import com.leoarmelin.connecta.ui.screens.GameScreen
import com.leoarmelin.connecta.ui.screens.StartScreen

@Composable
fun MainNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = StartScreen) {
        composable(StartScreen) {
            StartScreen(
                navController = navController,
            )
        }

        composable(GameScreen) {
            GameScreen(
                navController = navController,
            )
        }
    }
}