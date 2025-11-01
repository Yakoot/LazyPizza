package dev.mamkin.lazypizza.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dev.mamkin.lazypizza.app.navigation.NavigationRoot
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            LazyPizzaTheme {
                NavigationRoot()
            }
        }
    }
}