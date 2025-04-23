package com.example.cheatsheets.cheatsheets.backgroundwork.async.coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

// TODO
suspend fun main(): Unit = coroutineScope {
    delay(1000)
    println("Collecting . . .")
    channelFlowTest().collect {
        println(it)
    }
}

/**
 *
 */
fun defaultFlowTest() = flow {
    emit(1)
    emit(2)
    emit(3)
}.onEach { println("$it") }

fun channelFlowTest() = channelFlow {
    println("Printed after sub")
    launch {
        delay(1000)
        send(1)
    }
    launch {
        delay(1000)
        send(2)
    }
    launch {
        delay(1000)
        send(3)
    }

}
