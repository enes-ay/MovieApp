package com.enesay.movieapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.enesay.movieapp.ui.views.BottomNavItem


@Composable
fun BottomBarNavigation(navController: NavController) {
    val pages = listOf(
        BottomNavItem.Home,
        BottomNavItem.Profile,
        BottomNavItem.List
    )

    BottomNavigation(
        modifier = Modifier.padding(WindowInsets.navigationBars.asPaddingValues()), // Sistem çubuğu yüksekliğini ekler
        backgroundColor = Color.Black,
        contentColor = Color.White
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        pages.forEach { item ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == item.route } == true

            BottomNavigationItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = item.iconId),
                            contentDescription = stringResource(id = item.label),
                            tint = if (isSelected) Color.Blue else Color.Gray
                        )
                        Text(
                            text = stringResource(id = item.label),
                            style = MaterialTheme.typography.body2.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            ),
                            color = if (isSelected) Color.Blue else Color.Gray
                        )
                    }
                },
                selectedContentColor = Color.Blue,
                unselectedContentColor = Color.Gray,
                alwaysShowLabel = true
            )
        }
    }
}
