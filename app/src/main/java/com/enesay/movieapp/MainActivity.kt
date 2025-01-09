package com.enesay.movieapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.enesay.movieapp.ui.components.BottomBarNavigation
import com.enesay.movieapp.ui.navigation.AppNavigation
import com.enesay.movieapp.ui.theme.MovieAppTheme
import com.enesay.movieapp.ui.views.BottomNavItem
import com.enesay.movieapp.utils.DataStoreHelper
import com.enesay.movieapp.utils.setAppLocale
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userPreferencesDataStore = DataStoreHelper(context = this)

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val isDarkMode by userPreferencesDataStore.darkModeFlow.collectAsState(initial = false)

            MovieAppTheme (darkTheme = isDarkMode){
                Scaffold(modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        val showBottomBar = shouldShowBottomBar(navController = navController)
                        if (showBottomBar) {
                            BottomBarNavigation(navController = navController)
                        }

                    }) { paddingValues ->

                    AppNavigation(navController = navController, paddingValues = paddingValues)

                }
            }
        }

        // Set the app's language based on user preferences
        lifecycleScope.launch {
            userPreferencesDataStore.languageFlow.collect { savedLanguage ->
                if (savedLanguage != null) {
                    setAppLocale(this@MainActivity, savedLanguage)
                }
                else{
                    setAppLocale(this@MainActivity, "en")
                }
            }
        }
    }
}

// Control of bottom bar visibility
@Composable
private fun shouldShowBottomBar(navController: NavController): Boolean {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    return when (currentDestination?.route) {
        BottomNavItem.Home.route, BottomNavItem.Favorites.route, BottomNavItem.Cart.route, BottomNavItem.Profile.route -> true
        else -> false
    }
}