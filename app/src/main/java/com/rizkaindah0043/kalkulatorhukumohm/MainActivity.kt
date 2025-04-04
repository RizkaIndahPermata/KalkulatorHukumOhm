package com.rizkaindah0043.kalkulatorhukumohm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rizkaindah0043.kalkulatorhukumohm.navigation.SetupNavGraph
import com.rizkaindah0043.kalkulatorhukumohm.ui.theme.KalkulatorHukumOhmTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KalkulatorHukumOhmTheme {
                SetupNavGraph()
            }
        }
    }
}

