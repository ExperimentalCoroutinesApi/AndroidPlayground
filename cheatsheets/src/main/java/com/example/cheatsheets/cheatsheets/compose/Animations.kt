package com.example.cheatsheets.cheatsheets.compose


import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.TargetBasedAnimation
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

///**
// * Low-level animations
// * value state animations
// */
//const val START_VALUE = 0f
//const val END_VALUE = 300f
//
//@Composable
//fun AnimatableTest2() { // 1 - Animatable (float and Color)
//    val yOffsetAnimatable = remember { Animatable(START_VALUE) }
//
////    Image(
////        painter = painterResource(),
////        modifier = Modifier
////            .size(200.dp)
////            .offset(y = yOffsetAnimatable.value.dp)
////    )
//
//    LaunchedEffect(Unit) {
//        yOffsetAnimatable.animateTo( // suspend method!!!
//            targetValue = END_VALUE,
//            animationSpec = tween(1600)
//        )
//    }
//}
//
//@Composable
//fun AnimateAsStateTest( // 2 - animate*AsState
//    modifier: Modifier
//) {
//    val someState by produceState(
//        initialValue = false
//    ) {
//        repeat(10) {
//            delay(1000)
//            value = !value
//        }
//    }
//
//    val xOffset by animateFloatAsState(
//        if (someState) END_VALUE else START_VALUE,
//        animationSpec = tween(4_00),
//        label = "xOffset"
//    )
//
////    Image(
////        painter = painterResource(R.drawable.ic_launcher_background),
////        contentDescription = null,
////        modifier = modifier
////            .size(200.dp)
////            .offset(y = xOffset.dp)
////    )
//}
//
//@Composable
//fun UpdateTransitionTest( // 3 - updateTransition
//    modifier: Modifier
//) {
//    val someState = produceState(
//        initialValue = false
//    ) {
//        repeat(10) {
//            delay(1000)
//            value = !value
//        }
//    }
//
//    val transition = updateTransition(
//        someState,
//        label = "xOffset"
//    )
//
//    val xOffset by transition.animateDp { state ->
//        if (state.value) END_VALUE_DP else START_VALUE_DP
//    }
//
//    val yOffset by transition.animateDp { state ->
//        if (state.value) END_VALUE_DP + 100.dp else START_VALUE_DP
//    }
//
////    Image(
////        painter = painterResource(R.drawable.ic_launcher_background),
////        contentDescription = null,
////        modifier = modifier
////            .size(200.dp)
////            .offset(x = yOffset, y = xOffset)
////    )
//}
//
//val START_VALUE_DP = 0.dp
//val END_VALUE_DP = 300.dp
//
//@Composable
//fun InfiniteTransitionTest() { // 4 - infiniteTransition
//    val infiniteTransition = rememberInfiniteTransition(label = "infiniteTransition")
//
//    val color by infiniteTransition.animateColor(
//        initialValue = Color.Red,
//        targetValue = Color.Green,
//        animationSpec = infiniteRepeatable(
//            animation = tween(1000, easing = LinearEasing),
//            repeatMode = RepeatMode.Reverse
//        ),
//        label = "color"
//    )
//
//    Box(
//        Modifier
//            .fillMaxSize()
//            .background(color)
//    )
//}
//
///**
// * Animation: Manually controlled animation
// * Animation is the lowest-level Animation API available. Many of the animations we've seen
// * so far build ontop of Animation. There are two Animation subtypes: TargetBasedAnimation
// * and DecayAnimation.
// *
// * Animation should only be used to manually control the time of the animation. Animation is
// * stateless, and it does not have any concept of lifecycle. It serves as an animation
// * calculation engine that the higher-level APIs use.
// */
//@Composable
//fun TargetBasedAnimationTest() { // 5 - Animation(TargetBasedAnimation)
//    val anim = remember {
//        TargetBasedAnimation(
//            animationSpec = tween(200),
//            typeConverter = Float.VectorConverter,
//            initialValue = 200f,
//            targetValue = 1000f
//        )
//    }
//    var playTime by remember { mutableLongStateOf(0L) }
//
////    LaunchedEffect(anim) {
////        val startTime = withFrameNanos { it }
////
////        do {
////            playTime = withFrameNanos { it } - startTime
////            val animationValue = anim.getValueFromNanos(playTime)
////        } while (true)
////    }
//}
//
///**
// * 6 - DecayAnimation
// * Unlike TargetBasedAnimation, DecayAnimation does not require a targetValue to be provided.
// * Instead, it calculates its targetValue based on the starting conditions, set by
// * initialVelocity and initialValue and the supplied DecayAnimationSpec.
// *
// * Decay animations are often used after a fling gesture to slow elements down to a stop.
// * The animation velocity starts at the value set by initialVelocityVector and slows down over time.
// */
//
//
///**
// * High-level
// * Animation modifiers and composables
// */
//@Composable
//fun AnimateContentSizeTest( // 7 - animateContentSize
//    modifier: Modifier
//) {
//    val someState by produceState(
//        initialValue = false
//    ) {
//        repeat(10) {
//            delay(1000)
//            value = !value
//        }
//    }
//
//    Box(
//        modifier = modifier
//            .background(Color.Red)
//            .animateContentSize()
//            .height(if (someState) 400.dp else 200.dp)
//            .fillMaxWidth()
//    )
//}
//
//@Composable
//fun AnimatedVisibilityTest( // 8 - AnimatedVisibility
//    modifier: Modifier
//) {
//    val someState by produceState(
//        initialValue = false
//    ) {
//        repeat(10) {
//            delay(1000)
//            value = !value
//        }
//    }
//
//    val density = LocalDensity.current
//    AnimatedVisibility(
//        visible = someState,
//        enter = slideInVertically {
//            // Slide in from 40 dp from the top.
//            with(density) { -40.dp.roundToPx() }
//        } + expandVertically(
//            // Expand from the top.
//            expandFrom = Alignment.Top
//        ) + fadeIn(
//            // Fade in with the initial alpha of 0.3f.
//            initialAlpha = 0.3f
//        ),
//        exit = slideOutVertically() + shrinkVertically() + fadeOut()
//    ) {
//        Text(
//            text = "Hello",
//            fontSize = 100.sp,
//            modifier = modifier
//                .fillMaxWidth()
//                .height(200.dp)
//        )
//    }
//}
//
//@Composable
//fun AnimatableTest134134( // 9 - AnimatedContent
//    modifier: Modifier
//) {
//    val someState by produceState(
//        initialValue = 0
//    ) {
//        repeat(10) {
//            delay(1000)
//
//            value += 1
//        }
//    }
//
//    Row(
//        modifier = modifier
//    ) {
//
//        AnimatedContent(
//            targetState = someState,
//            label = "animated content",
//            transitionSpec = {
//                // Compare the incoming number with the previous number.
//                if (targetState > initialState) {
//                    // If the target number is larger, it slides up and fades in
//                    // while the initial (smaller) number slides up and fades out.
//                    slideInVertically { height -> height } + fadeIn() togetherWith
//                            slideOutVertically { height -> -height } + fadeOut()
//                } else {
//                    // If the target number is smaller, it slides down and fades in
//                    // while the initial number slides down and fades out.
//                    slideInVertically { height -> -height } + fadeIn() togetherWith
//                            slideOutVertically { height -> height } + fadeOut()
//                }.using(
//                    // Disable clipping since the faded slide-in/out should
//                    // be displayed out of bounds.
//                    SizeTransform(clip = false)
//                )
//            }
//        ) { targetCount ->
//
//            Text(
//                text = "$targetCount",
//                fontSize = 100.sp
//            )
//        }
//    }
//}
//
///**
// *
// */