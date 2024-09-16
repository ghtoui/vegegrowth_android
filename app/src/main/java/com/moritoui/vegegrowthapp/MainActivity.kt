package com.moritoui.vegegrowthapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.ktx.Firebase
import com.moritoui.vegegrowthapp.core.analytics.AnalyticsHelper
import com.moritoui.vegegrowthapp.core.analytics.LocalAnalyticsHelper
import com.moritoui.vegegrowthapp.ui.admob.AdmobBanner
import com.moritoui.vegegrowthapp.ui.main.MainScreen
import com.moritoui.vegegrowthapp.ui.theme.VegegrowthAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    @Inject lateinit var analytics: AnalyticsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        firebaseAnalytics = Firebase.analytics
        firebaseEventSend(firebaseAnalytics)

        MobileAds.initialize(this) {}
        val context = this
        val banner =
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                adUnitId = BuildConfig.AD_BANNER_UNIT_ID
                loadAd(AdRequest.Builder().build())
            }
        setContent {
            CompositionLocalProvider(
                LocalAnalyticsHelper provides analytics
            ) {
                VegegrowthAppTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Column {
                            MainScreen(
                                modifier = Modifier.weight(1f),
                                firebaseAnalytics
                            )
                            AdmobBanner(
                                modifier = Modifier
                                    .padding(
                                        bottom = WindowInsets.navigationBars.asPaddingValues()
                                            .calculateBottomPadding()
                                    ),
                                banner = banner,
                                context = context
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun firebaseEventSend(firebaseAnalytics: FirebaseAnalytics) {
    // ユーザがアプリを起動したときにログを取る
    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN) {
        Bundle().apply {
            putString(FirebaseAnalytics.Param.METHOD, "launch_app")
        }
    }
}
