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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.enesay.movieapp.ui.components.BottomBarNavigation
import com.enesay.movieapp.ui.navigation.AppNavigation
import com.enesay.movieapp.ui.theme.MovieAppTheme
import com.enesay.movieapp.ui.views.BottomNavItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            MovieAppTheme {
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
    }
}

// control of bottom bar visibility
@Composable
private fun shouldShowBottomBar(navController: NavController): Boolean {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    return when (currentDestination?.route) {
        BottomNavItem.Home.route, BottomNavItem.List.route, BottomNavItem.Profile.route -> true
        else -> false
    }
}