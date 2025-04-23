package com.example.cheatsheets.cheatsheets.backgroundwork.async.coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import kotlin.random.Random

/**
 * ============================= Dispatchers =============================
 *
 * Default (CPU-intensive calculations)
 * Main (if we use the kotlinx-coroutines-android artifact)
 * IO (when we block threads withI/O operations)
 * Unconfined (When it is started, it runs on the thread on
 * which it was started. If it is resumed, it runs on the thread that
 * resumed it.)
 *
 * Both Dispatchers.Default and Dispatchers.IO share the same pool of threads.
 *
 * Default is Dispatchers.Default (CPU-intensive calculations)
 * It has a pool of threads with a size equal to the number
 * of cores in the machine your code is running on (but not less than
 * two)
 */
suspend fun defaultDispatcher() = coroutineScope {
    repeat(1000) {
        launch { // or launch(Dispatchers.Default) {
            // To make it busy
            List(1000) { Random.nextLong() }.maxOrNull()
            val threadName = Thread.currentThread().name
            println("Running on thread: $threadName")
        }
    }
}
// Running on thread: DefaultDispatcher-worker-4
// Running on thread: DefaultDispatcher-worker-10
// Running on thread: DefaultDispatcher-worker-5 . . .

/**
 * We can use
 * limitedParallelism on Dispatchers.Default to make a dispatcher
 * that runs on the same threads but is limited to using not more than
 * a certain number of them at the same time.
 */
@OptIn(ExperimentalCoroutinesApi::class)
private val dispatcher = Dispatchers.Default.limitedParallelism(5)

/**
 * The limit of Dispatchers.IO is 64 (or the number of cores
 * if there are more)
 */
suspend fun ioDispatcher() = coroutineScope {
    repeat(1000) {
        launch(Dispatchers.IO) {
            Thread.sleep(200)
            val threadName = Thread.currentThread().name
            println("Running on thread: $threadName")
        }
    }
}
// Running on thread: DefaultDispatcher-worker-1
//...
// Running on thread: DefaultDispatcher-worker-53
// Running on thread: DefaultDispatcher-worker-64

/**
 * Dispatchers.IO has a special behavior defined for the
 * limitedParallelism function. It creates a new dispatcher with
 * an independent pool of threads. What is more, this pool is not
 * limited to 64 as we can decide to limit it to as many threads as we
 * want.
 */
@OptIn(ExperimentalCoroutinesApi::class)
suspend fun ioLimited(): Unit = coroutineScope {
    launch {
        // some fun (Dispatchers.IO)
        // Dispatchers.IO took: 2074
    }
    launch {
        val dispatcher = Dispatchers.IO.limitedParallelism(100)
        // some fun (dispatcher)
        // LimitedDispatcher@XXX took: 1082
    }
}

// Dispatcher with a fixed pool of threads
val NUMBER_OF_THREADS = 20
val dispatcherTest = Executors
    .newFixedThreadPool(NUMBER_OF_THREADS)
    .asCoroutineDispatcher()

