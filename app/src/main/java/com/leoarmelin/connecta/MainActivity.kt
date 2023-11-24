package com.leoarmelin.connecta

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.leoarmelin.connecta.helpers.SharedPreferencesHelper
import com.leoarmelin.connecta.navigation.MainNavHost
import com.leoarmelin.connecta.ui.theme.ConnectaTheme
import com.leoarmelin.connecta.ui.theme.STATUS_BAR_SCRIM
import com.leoarmelin.connecta.ui.theme.gradient_background
import com.leoarmelin.connecta.viewmodels.WordsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@SuppressLint("SourceLockedOrientationActivity")
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val wordsViewModel: WordsViewModel by viewModels()
    @Inject
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = STATUS_BAR_SCRIM.hashCode()
        window.navigationBarColor = STATUS_BAR_SCRIM.hashCode()

        MobileAds.initialize(this)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setContent {
            ConnectaTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(brush = Brush.verticalGradient(gradient_background)),
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

        RewardedAd.load(
            this,
            BuildConfig.ADS_API_BLOCK_KEY,
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    ad.show(this@MainActivity) {
                        wordsViewModel.getDailyWords(true)
                        wordsViewModel.updateHasWonValue(false)
                    }
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.d("Aoba", "Error during ad loading: $error")
                }
            })
    }
}