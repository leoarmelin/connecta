package com.leoarmelin.connecta

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.leoarmelin.connecta.helpers.SharedPreferencesHelper
import com.leoarmelin.connecta.navigation.MainNavHost
import com.leoarmelin.connecta.ui.theme.ConnectaTheme
import com.leoarmelin.connecta.viewmodels.WordsViewModel

class MainActivity : ComponentActivity() {

    private val wordsViewModel by lazy {
        WordsViewModel(
            sharedPreferencesHelper = SharedPreferencesHelper(this.applicationContext)
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ConnectaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainNavHost(
                        wordsViewModel = wordsViewModel
                    )
                }
            }
        }
    }
}