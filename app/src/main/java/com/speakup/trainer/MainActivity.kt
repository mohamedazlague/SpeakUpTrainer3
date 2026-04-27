package com.speakup.trainer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.speakup.trainer.ui.navigation.NavGraph
import com.speakup.trainer.ui.theme.SpeakUpTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Single Activity that hosts the entire Jetpack Compose UI.
 * @AndroidEntryPoint enables Hilt injection in this Activity.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpeakUpTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    NavGraph()
                }
            }
        }
    }
}
