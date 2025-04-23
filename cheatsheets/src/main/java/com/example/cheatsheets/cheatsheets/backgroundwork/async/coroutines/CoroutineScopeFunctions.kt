package com.example.cheatsheets.cheatsheets.backgroundwork.async.coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull

/**
 * ============================= Coroutine scope functions =============================
 *
 * is a suspending function that starts a scope.
 * It returns the value produced by the argument function.
 *
 * Unlike async or launch, the body of coroutineScope is called in-place.
 * It formally creates a new coroutine, but it suspends the previous one
 * until the new one is finished, so it does not start any concurrent
 * process. Take a look at the below example, in which both delay calls
 * suspend runBlocking.
 *
 * supervisorScope is like coroutineScope but it uses SupervisorJob instead of Job.
 * withContext is a coroutineScope that can modify coroutine context.
 * withTimeout is a coroutineScope with a timeout.
 *
 * Choose scope functions over async+immediately await()!!
 */

suspend fun withContextError() = withContext(Dispatchers.IO) {
    launch {
        throw Error()
    }
    delay(1000)
    println("Will be printed? No!")
}

suspend fun coroutineScopeError() = coroutineScope {
    launch {
        throw Error()
    }
    delay(1000)
    println("Will be printed? No!")
}

suspend fun withTimeout() = kotlinx.coroutines.withTimeout(2000) {
    launch {
        throw Error()
    }
    delay(1000)
    println("Will be printed? No!")
}

suspend fun supervisorScopeError() = kotlinx.coroutines.supervisorScope {
    launch {
        throw Error()
    }
    delay(1000)
    println("Will be printed? Yes!")
}

/**
 * If you need to use functionalities from two coroutine scope functions,
 * you need to use one inside another. For instance, to set both
 * a timeout and a dispatcher, you can use withTimeoutOrNull inside
 * withContext.
 */
suspend fun doubleWithContext(): Int? =
    withContext(Dispatchers.Default) {
        withTimeoutOrNull(1000) {
            delay(300)
            66
        }
    }