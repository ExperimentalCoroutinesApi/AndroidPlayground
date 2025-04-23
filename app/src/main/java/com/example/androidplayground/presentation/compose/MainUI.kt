package com.example.androidplayground.presentation.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

@Composable
fun MainUI(
    modifier: Modifier = Modifier
) {
    val i by produceState(initialValue = "WOW") {
        delay(1000)
        value = "Oh...."
    }
    RememberUpdatedStateTest1(i)
}

@Composable
fun RememberUpdatedStateTest1(s: String) {

    LaunchedEffect(Unit) {
        delay(2000)
        println(s)
    }
}