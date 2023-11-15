package com.leoarmelin.connecta

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.PersistableBundle
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

@SuppressLint("SourceLockedOrientationActivity")
class MainActivity : ComponentActivity() {

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private val wordsViewModel by lazy {
        WordsViewModel(
            sharedPreferencesHelper = sharedPreferencesHelper
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MobileAds.initialize(this)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        sharedPreferencesHelper = SharedPreferencesHelper(this.applicationContext)

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

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        sharedPreferencesHelper.saveHasWon(wordsViewModel.hasWon.value)
        super.onSaveInstanceState(outState, outPersistentState)
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