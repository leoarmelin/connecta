package com.leoarmelin.connecta

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
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

        MobileAds.initialize(this)

        setContent {
            ConnectaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainNavHost(
                        wordsViewModel = wordsViewModel,
                        onLoadAd = ::showAd
                    )
                }
            }
        }
    }

    private fun showAd() {
        val adRequest = AdRequest.Builder().build()

        RewardedAd.load(this, BuildConfig.ADS_API_BLOCK_KEY, adRequest, object : RewardedAdLoadCallback() {
            override fun onAdLoaded(ad: RewardedAd) {
                ad.show(this@MainActivity) {
                    wordsViewModel.getDailyWords(true)
                    wordsViewModel.updateHasWonValue(emptySet(), false)
                }
            }

            override fun onAdFailedToLoad(error: LoadAdError) {
                Log.d("Aoba", "Error during ad loading: $error")
            }
        })
    }
}