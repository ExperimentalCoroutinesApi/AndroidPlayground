package com.example.androidplayground.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.androidplayground.presentation.compose.AutoAndManualScrollRow
import com.example.androidplayground.ui.theme.AndroidPlaygroundTheme
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentSet
import kotlin.random.Random

class MainActivity : ComponentActivity() {

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            AndroidPlaygroundTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    val start = remember { System.nanoTime() }
//                    Box(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(innerPadding)
//                            .onGloballyPositioned {
//                                val elapsed = (System.nanoTime() - start) / 1_000_000
//                                Log.d("THIS IS MY LOG", "Rendered in $elapsed ms")
//                            }
//                    ) {
//                        ScrollableHorizontalGrid(
//                            modifier = Modifier
//                                .align(Alignment.Center)
//                                .background(
//                                    Color(0xFFFFFFFF)
//                                )
//                                .border(1.dp, Color.Black)
//                                .align(Alignment.Center),
//                            horizontalSpacing = 6.dp,
//                            verticalSpacing = 6.dp,
//                            rows = 3
//                        ) {
//                            repeat(ELEMENTS_COUNT) {
//                                Box(
//                                    modifier = Modifier
//                                        .width(Random.nextInt(50, 200).dp)
//                                        .height(50.dp)
//                                        .clip(RoundedCornerShape(percent = 50))
//                                        .background(Color(Random.nextLong(0xFFFFFFFF)))
//                                        .border(1.dp, Color.Black, RoundedCornerShape(percent = 50))
//                                ) {
//                                    Box(
//                                        modifier = Modifier
//                                            .size(30.dp)
//                                            .clip(RoundedCornerShape(percent = 50))
//                                            .background(Color.White)
//                                            .align(Alignment.Center)
//                                            .border(
//                                                1.dp,
//                                                Color.Black,
//                                                RoundedCornerShape(percent = 50)
//                                            )
//                                    ) {
//                                        Text(
//                                            text = it.toString(),
//                                            fontFamily = FontFamily.Serif,
//                                            color = Color.Black,
//                                            modifier = Modifier.align(Alignment.Center)
//                                        )
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidPlaygroundTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val start = remember { System.nanoTime() }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .onGloballyPositioned {
                                val elapsed = (System.nanoTime() - start) / 1_000_000
                                Log.d("THIS IS MY LOG", "Rendered in $elapsed ms")
                            }
                    ) {
                        val items = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")
                        AutoAndManualScrollRow(
                            items, {}
                        )
                    }
                }
            }
        }
    }
}

const val ELEMENTS_COUNT = 34

data class GridItem(val width: Dp, val color: Color)