package com.example.cheatsheets.cheatsheets.backgroundwork.async.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.EmptyCoroutineContext


/**
 * ============================= The problem with shared state =============================
 *
 *  We should use coroutine-specific tools. Ones that do not block but instead suspend or avoid conflict
 *
 * Bad approach:
 *
 *  - Blocking synchronization is bad because of blocking threads when a coroutine is waiting for its turn
 * -------------------------
 * Good approach:
 *
 *  - Atomics. Their operations are implemented at a low level without locks
 *  We often use atomics to secure a single primitive or a single reference,
 *  but for more complicated cases we still need better tools.
 *
 * - A dispatcher limited to a single thread.  This is the easiest solution for most problems
 * with shared states. Use Dispatchers.Default or Dispatchers.IO (if we
 * block threads) with parallelism limited to 1.
 *
 * Fine-grained thread confinement - In this approach, we wrap only those statements which modify the state
 * Coarse-grained thread confinement. This is an easy approach whereby we just wrap the whole function with withContext
 *
 * - Mutex. The important advantage of mutex over a synchronized block is
 * that we suspend a coroutine instead of blocking a thread. This is
 * a safer and lighter approach.
 *
 * - Semaphore for coroutines. Semaphore with more than one permit does not help us with the
 * problem of shared state, but it can be used to limit the number of
 * concurrent requests, so to implement rate limiting.
 */
suspend fun problem() {
    var x: Int = 0
    coroutineScope {
        repeat(1_000_000) {
            launch {
                x++ // race condition
            }
        }
    }
    print(x) // ~998786
}

suspend fun run(action: suspend () -> Unit) =
    withContext(Dispatchers.Default) {
        repeat(1000) {
            launch {
                repeat(1000) { action() }
            }
        }
    }


// Blocking synchronization
var counter = 0
fun synchronized() = runBlocking {
    val lock = Any()
    run {
        synchronized(lock) { // We are blocking threads!
            counter++
        }
    }
    println("Counter = $counter") // 1000000
}

// Atomics
var counter2 = AtomicInteger()
fun atomics() = runBlocking {
    run {
        counter2.incrementAndGet()
    }
    println(counter2.get()) // 1000000
}

// A dispatcher limited to a single thread - Best solution
@OptIn(ExperimentalCoroutinesApi::class)
val singleThreadDispatcher = Dispatchers.IO
    .limitedParallelism(1)
var counter3 = 0
fun singleThreadDispatcher() = runBlocking {
    run {
        withContext(singleThreadDispatcher) {
            counter3++
        }
    }
    println(counter3) // 1000000
}

//Mutex
val mutex = Mutex()
var counter4 = 0
fun mutexTest() = runBlocking {
    run {
        mutex.withLock {
            counter4++
        }
    }
    println(counter4) // 1000000
}

// Semaphore
suspend fun semaphore() = coroutineScope {
    val semaphore = Semaphore(2)
    repeat(5) {
        launch {
            semaphore.withPermit {
                delay(1000)
                print(it)
            }
        }
    }
}



