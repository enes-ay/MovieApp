package com.enesay.movieapp.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp


@Composable
fun MovieDetail(){

    Column(modifier = Modifier.fillMaxSize()) {
        Text("movie detail", fontSize = 34.sp)
    }
}