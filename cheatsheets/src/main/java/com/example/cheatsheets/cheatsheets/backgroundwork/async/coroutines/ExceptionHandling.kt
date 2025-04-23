package com.example.cheatsheets.cheatsheets.backgroundwork.async.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * ============================= Exception handling =============================
 *
 *
 * Once a coroutine receives an exception, it
 * cancels itself and propagates the exception to its parent (launch).
 * Exception propagation is bi-directional: the exception is propagated
 * from child to parent, and when those parents are cancelled, they
 * cancel their children. Thus, if exception propagation is not stopped,
 * all coroutines in the hierarchy will be cancelled.
 *
 * If an exception is a subclass of CancellationException, it will not
 * be propagated to its parent. It will only cause cancellation of the
 * current coroutine. CancellationException is an open class, so it can
 * be extended by our own classes or objects.
 */

fun errorCancelingTheHierarchy(): Unit = runBlocking { // 1 - Error Canceling The Hierarchy
    launch {
        launch {
            delay(1000)
            throw Error("Some error")
        }
        launch {
            delay(2000)
            println("Will not be printed")
        }
        launch {
            delay(500) // faster than the exception
            println("Will be printed")
        }
    }
    launch {
        delay(2000)
        println("Will not be printed")
    }
}
// Will be printed
// Exception in thread "main" java.lang.Error: Some error...

/**
 * This is a special kind of job that ignores all exceptions
 * in its children. - SupervisorJob()
 */
fun supervisorJob(): Unit = runBlocking { // 2 - Supervisor Job
    val scope = CoroutineScope(SupervisorJob())
    scope.launch {
        delay(1000)
        throw Error("Some error")
    }
    scope.launch {
        delay(2000)
        println("Will be printed")
    }
    delay(3000)
}
// Exception...
// Will be printed

fun supervisorScope(): Unit = runBlocking { // 3 - Supervisor Scope

    // is just a suspending function and can be used to
    // wrap suspending function bodies
    kotlinx.coroutines.supervisorScope {
        launch {
            delay(1000)
            throw Error("Some error")
        }
        launch {
            delay(2000)
            println("Will be printed")
        }
    }
    delay(1000)
    println("Done")
}
// Exception...
// Will be printed
// (1 sec)
// Done

/**
 * CancellationException does not propagate to its
 * parent
 */
suspend fun cancellationException(): Unit = coroutineScope { // 4 - Cancellation Exception
    launch {
        launch {
            delay(2000)
            println("Will not be printed")
        }
        throw CancellationException()
    }
    launch {
        delay(2000)
        println("Will be printed")
    }
}
// (2 sec)
// Will be printed

fun coroutineExceptionHandler(): Unit = runBlocking { // 5 - Coroutine Exception Handler
    val handler =
        CoroutineExceptionHandler { _, exception ->
            println("Caught $exception")
        }
    val scope = CoroutineScope(SupervisorJob() + handler)
    scope.launch {
        delay(1000)
        throw Error("Some error")
    }
    scope.launch {
        delay(2000)
        println("Will be printed")
    }
    delay(3000)
}
// Caught java.lang.Error: Some error
// Will be printed