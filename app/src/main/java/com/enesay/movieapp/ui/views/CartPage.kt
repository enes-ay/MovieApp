package com.enesay.movieapp.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun CartPage(navController: NavController){
    Column(modifier = Modifier.fillMaxSize()) {
        Text("cart", fontSize = 34.sp)
    }

}