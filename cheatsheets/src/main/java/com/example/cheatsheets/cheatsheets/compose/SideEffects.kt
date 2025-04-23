package com.example.cheatsheets.cheatsheets.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProduceStateScope
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * Еffects.kt is a part of package androidx.compose.runtime
 *
 */


/**
 * Record a function to call when changes to the corresponding tree are applied to the applier.
 * This is used to implement SideEffect.
 *
 * currentComposer.recordSideEffect(effect)
 */
@Composable
fun SideEffectTest() { // 1

    SideEffect {
        //analytics.setUserProperty("userType", user.userType)
    }
}

@Composable
fun LaunchedEffectTest() { // 2

    LaunchedEffect(Unit) {
        while (isActive) {
            //(pulseRateMs) // Pulse the alpha every pulseRateMs to alert the user
            //alpha.animateTo(0f)
            //alpha.animateTo(1f)
        }
    }
}

@Composable
fun RememberCoroutineScopeTest() { // 3

    //val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = {
            //SnackbarHost(hostState = snackbarHostState)
        }
    ) { contentPadding ->
        Column(Modifier.padding(contentPadding)) {
            Button(
                onClick = {
                    // Create a new coroutine in the event handler to show a snackbar
                    //scope.launch {
                        //snackbarHostState.showSnackbar("Something happened!")
                    //}
                }
            ) {
                Text("Press me")
            }
        }
    }
}


//@Composable
//fun DisposableEffectTest( // 4
//    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
//    onStart: () -> Unit, // Send the 'started' analytics event
//    onStop: () -> Unit // Send the 'stopped' analytics event
//) {
//    // Safely update the current lambdas when a new one is provided
//    val currentOnStart by rememberUpdatedState(onStart)
//    val currentOnStop by rememberUpdatedState(onStop)
//
//    // If `lifecycleOwner` changes, dispose and reset the effect
//    DisposableEffect(lifecycleOwner) {
//        // Create an observer that triggers our remembered callbacks
//        // for sending analytics events
//        val observer = LifecycleEventObserver { _, event ->
//            if (event == Lifecycle.Event.ON_START) {
//                currentOnStart()
//            } else if (event == Lifecycle.Event.ON_STOP) {
//                currentOnStop()
//            }
//        }
//
//        // Add the observer to the lifecycle
//        lifecycleOwner.lifecycle.addObserver(observer)
//
//        // When the effect leaves the Composition, remove the observer
//        onDispose {
//            lifecycleOwner.lifecycle.removeObserver(observer)
//        }
//    }
//
//    /* Home screen content */
//}

/**
 * You should use the derivedStateOf function when your inputs to a composable are changing
 * more often than you need to recompose.
 *
 * Caution: derivedStateOf is expensive, and you should only use it to avoid unnecessary
 * recomposition when a result hasn't changed.
 */
@Composable
fun DerivedStateOfTest() { // 5
    val listState by remember { mutableStateOf(listOf(1)) }
    val showButton by remember {
        derivedStateOf {
            listState.first() > 0
        }
    }
}

/**
 * Use snapshotFlow to convert State<T> objects into a cold Flow. snapshotFlow runs
 * its block when collected and emits the result of the State objects read in it.
 */
@Composable
fun SnapshotFlowTest() { // 6

    val listState = rememberLazyListState()

    LazyColumn(state = listState) {
        // ...
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .map { index -> index > 0 }
            .distinctUntilChanged()
            .filter { it }
            .collect {
                //MyAnalyticsService.sendScrolledPastFirstItemEvent()
            }
    }
}

/**
 * produceState launches a coroutine scoped to the Composition that can push values into a
 * returned State. Use it to convert non-Compose state into Compose state
 */
@Composable
fun ProduceStateTest() { // 7

    val message by produceState(
        initialValue = "initial text"
    ) {
        delay(2000)
        value = "hello!"
    } // = LaunchedEffect + remembered value

    Text(text = message)
} // Basically you can make remember { mutableState } and update it from LaunchedEffect


/**
 * LaunchedEffect restarts when one of the key parameters changes. However, in some situations
 * you might want to capture a value in your effect that, if it changes, you do not want the
 * effect to restart. In order to do this, it is required to use rememberUpdatedState to create
 * a reference to this value which can be captured and updated. This approach is helpful for
 * effects that contain long-lived operations that may be expensive or prohibitive to recreate
 * and restart.
 */
@Composable
fun MainUI() {
    val i by produceState(initialValue = "Initial value") {
        delay(1000)
        value = "Oh.... This value never came"
    }
    RememberUpdatedStateTest1(i)
}

@Composable
fun RememberUpdatedStateTest1(s: String) { // 8

    LaunchedEffect(Unit) {
        delay(2000)
        println(s)
    }
}

// Switch to

@Composable
fun RememberUpdatedStateTest2(s: String) { // 8

    val coolStuff by rememberUpdatedState(newValue = s)
    LaunchedEffect(Unit) {
        delay(2000)
        println(s) // now we will see "Oh.... This value never came"
    }
}