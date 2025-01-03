package com.enesay.movieapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.enesay.movieapp.ui.views.BottomNavItem


@Composable
fun BottomBarNavigation(
    navController: NavController) {
    val pages = listOf(
        BottomNavItem.Home,
        BottomNavItem.Profile,
        BottomNavItem.List
    )
    BottomAppBar(
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
                } },
                icon = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = item.iconId),
                            contentDescription = null,
                            tint = if (isSelected) Color.Black else Color.White
                        )
                        Text(
                            text = stringResource(id = item.label),
                            style = MaterialTheme.typography.body2,
                            color = if (isSelected) Color.Black else Color.White
                        )
                    }
                },
                alwaysShowLabel = true, // Ensures title is shown
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.White
            )
        }
    }
}