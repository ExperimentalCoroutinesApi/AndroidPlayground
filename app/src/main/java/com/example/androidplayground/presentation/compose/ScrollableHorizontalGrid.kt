package com.example.androidplayground.presentation.compose

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun AutoAndManualScrollRow(
    examples: List<String>,
    onExampleClick: (String) -> Unit
) {
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        while (scrollState.value != scrollState.maxValue) {
            delay(16)
            scrollState.scrollTo(scrollState.value + 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .horizontalScroll(scrollState)
            .clipToBounds()
    ) {

        Row {
            examples.forEach { example ->
                Button(
                    onClick = { onExampleClick(example) },
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text(example)
                }
            }
        }
    }
}

fun <T> List<T>.splitIntoParts(parts: Int): List<List<T>> {
    // Fail fast
    if (parts <= 0) return emptyList()

    val baseSize = this.size / parts
    val remainder = this.size % parts

    var index = 0
    return List(parts) { i ->
        val extra = if (i < remainder) 1 else 0
        val size = baseSize + extra
        val sublist = this.subList(index, index + size)
        index += size
        sublist
    }
}

fun List<List<Placeable>>.calculateLayoutInfo(
    horizontalSpacing: Int,
    verticalSpacing: Int
): LayoutInfo {
    val rowHeights = this.map { group ->
        group.maxOfOrNull { it.height } ?: 0
    }

    val totalHeight = rowHeights.sum() + verticalSpacing * (this.size - 1).coerceAtLeast(0)

    val maxWidth = this.maxOfOrNull { group ->
        val spacing = horizontalSpacing * (group.size - 1).coerceAtLeast(0)
        group.sumOf { it.width } + spacing
    } ?: 0

    return LayoutInfo(width = maxWidth, height = totalHeight, rowHeights = rowHeights)
}

data class LayoutInfo(
    val width: Int,
    val height: Int,
    val rowHeights: List<Int>
)