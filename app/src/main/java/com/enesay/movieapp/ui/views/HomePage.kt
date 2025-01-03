package com.enesay.movieapp.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun HomePage(navController: NavController){

    Scaffold(modifier = Modifier.fillMaxSize(),) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                columns = GridCells.Fixed(2)
            ){
                items(10){
                    Text(text = "item $it")
                }

            }

        }
    }

}