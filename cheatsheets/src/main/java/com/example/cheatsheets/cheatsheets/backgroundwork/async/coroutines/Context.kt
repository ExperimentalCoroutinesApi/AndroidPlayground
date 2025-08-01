package com.example.cheatsheets.cheatsheets.backgroundwork.async.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.EmptyCoroutineContext


/**
 * GlobalScope is CoroutineScope(EmptyCoroutineContext)
 * viewModelScope is CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate) // Main.immediate не переключается, если ты уже на главном потоке.
 * lifecycleScope is CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
 *
 * There is a very important rule: Job is the only coroutine context that
 * is not inherited by a coroutine from a coroutine. Every coroutine creates its own Job,
 * and the job from an argument or parent coroutine
 * is used as a parent of this new job.
 *
 */
fun main(): Unit = runBlocking {
    val name = CoroutineName("Some name")
    val job = Job()
    launch(name + job) {
        val childName = coroutineContext[CoroutineName]
        println(childName == name) // true
        val childJob = coroutineContext[Job]
        println(childJob == job) // false
        println(childJob == job.children.first()) // true
    }
}

fun test(): Unit = runBlocking {
// Don't do that, SupervisorJob with one children
// and no parent works similar to just Job
    launch(SupervisorJob()) { // 1
        launch {
            delay(1000)
            throw Error("Some error")
        }
        launch {
            delay(2000)
            println("Will not be printed")
        }
    }
    delay(3000)
}
// Exception...

