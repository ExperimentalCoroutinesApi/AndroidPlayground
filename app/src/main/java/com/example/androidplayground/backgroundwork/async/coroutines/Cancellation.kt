package com.example.androidplayground.backgroundwork.async.coroutines

import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import kotlin.random.Random

/**
 * ============================= Basic Cancellation =============================
 *
 * The Job interface has a cancel method, which allows its cancellation.
 * Calling it triggers the following effects:
 *
 * • Such a coroutine ends the job at the first suspension point
 * (delay in the example below).
 * • If a job has some children, they are also cancelled (but its
 * parent is not affected).
 * • Once a job is cancelled, it cannot be used as a parent for any
 * new coroutines. It is first in the “Cancelling” and then in the
 * “Cancelled” state.
 */
suspend fun basicCancellation(): Unit = coroutineScope { // 1 - Basic Cancellation
    val job = Job()
    launch(job) {
        repeat(1_000) { i ->
            delay(200)
            println("Printing $i")
        }
    }
    delay(1100)
    job.cancelAndJoin() // To avoid race conditions
    println("Cancelled successfully")
}
// Printing 0
// Printing 1
// Printing 2
// Printing 3
// Printing 4
// Cancelled successfully

/**
 * ============================= Clean Up The Resources =============================
 */
suspend fun nonCancellableFinallyBlock(): Unit = coroutineScope { // 2 - Non Cancellable Finally Block
    val job = Job()
    launch(job) {
        try {
            delay(200)
            println("Coroutine finished")
        } finally {
            println("Finally")
            withContext(NonCancellable) {
                delay(1000L)
                println("Cleanup done")
            }
        }
    }
    delay(100)
    job.cancelAndJoin()
    println("Done")
}
// Finally
// Cleanup done
// Done

/**
 * Null if the job finished with no exception;
 * • CancellationException if the coroutine was cancelled;
 * • the exception that finished a coroutine
 */
suspend fun invokeOnCompletion(): Unit = coroutineScope { // 3 - Invoke On Completion
    val job = launch {
        delay(Random.nextLong(2400))
        yield()
        println("Finished")
    }
    delay(800)
    job.invokeOnCompletion { exception: Throwable? ->
        println("Will always be printed")
        println("The exception was: $exception")
    }
    delay(800)
    job.cancelAndJoin()
}
// Will always be printed
// The exception was:
// kotlinx.coroutines.JobCancellationException
// (or)
// Finished
// Will always be printed
// The exception was null

/**
 * ============================= Stopping the unstoppable =============================
 *
 *
 * The function ensureActive() needs to be called
 * on a CoroutineScope (or CoroutineContext, or Job). All it does is
 * throw an exception if the job is no longer active. It is lighter, so
 * generally it should be preferred.
 *
 * The function yield is a regular
 * top-level suspension function. It does not need any scope, so it can
 * be used in regular suspending functions. Since it does suspension
 * and resuming, other effects might arise, such as thread changing if
 * we use a dispatcher with a pool of threads (more about this in the
 * Dispatchers chapter). yield is more often used just in suspending
 * functions that are CPU intensive or are blocking threads.
 */

suspend fun ensureActiveStopping(): Unit = coroutineScope { // 4 - Ensure Active Stopping
    val job = Job()
    launch(job) {
        repeat(1000) { num ->
            Thread.sleep(200)
            ensureActive()
            println("Printing $num")
        }
    }
    delay(1100)
    job.cancelAndJoin()
    println("Cancelled successfully")
}
// Printing 0
// Printing 1
// Printing 2
// Printing 3
// Printing 4
// Cancelled successfully

suspend fun yieldStopping(): Unit = coroutineScope { // 5 - Yield Stopping
    val job = Job()
    launch(job) {
        repeat(1_000) { i ->
            Thread.sleep(200)

            // Yield accomplishes at least a few things
            //
            // 1) It temporarily deprioritises the current long running CPU task,
            // giving other tasks a fair opportunity to run.
            // 2) Checks whether the current job is cancelled, since otherwise in a tight
            // CPU bound loop, the job may not check until the end.
            // 3) Allows for progress of child jobs, where there is contention because more jobs than threads.
            // This may be important where the current job should adapt based on progress of other jobs.
            yield()

            println("Printing $i")
        }
    }
    delay(1100)
    job.cancelAndJoin()
    println("Cancelled successfully")
    delay(1000)
}
// Printing 0
// Printing 1
// Printing 2
// Printing 3
// Printing 4
// Cancelled successfully