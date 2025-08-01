package com.example.cheatsheets.cheatsheets.backgroundwork.async.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.logging.Logger
import kotlin.coroutines.EmptyCoroutineContext

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

fun flowTest() = flow {
    emit(
        listOf(1,2,3,4,5) // ← выполняется на Dispatchers.Default
    )
}
    .flowOn(Dispatchers.Default) // ← применяется к flow выше
    .map { list -> // ← выполняется на IO
        list.map { number ->
            number * number
        }
    }
    .flowOn(Dispatchers.IO) // ← применяется к .map {...}
    .onCompletion { println("Значения заэмичены") }

suspend fun foo(): String = "afdadgfhagdgadh"

suspend fun doCollect() {

    flowTest()
        .onStart {
            //_uiState.value = UsersUiState.Loading
        }
        .catch { e ->
            //_uiState.value = UsersUiState.Error(e.message ?: "Неизвестная ошибка")
        }
        .onCompletion {
            // это перепишет верхний он комплишн
        }
        .collect { users ->
            //_uiState.value = UsersUiState.Success(users)
        }

    /**
     * Вот так мы можем объединить наши источники в одно UDF состояние.
     */
    val uiState: StateFlow<HomeUiState> = combine(
        flowTest(),
        flowTest(),
        flowTest()
    ) { notifications, event, settings ->
        HomeUiState(
            notifications = notifications,
            event = event,
            userSettings = settings
        )
    }.stateIn(
        scope = CoroutineScope(EmptyCoroutineContext), //viewModelScope
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState(
            emptyList(), emptyList(), emptyList()
        )
    )
}

data class HomeUiState(
    val notifications: List<Int> = emptyList(),
    val event: List<Int>,
    val userSettings: List<Int>
)