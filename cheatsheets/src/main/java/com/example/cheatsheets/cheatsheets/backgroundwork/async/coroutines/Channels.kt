package com.example.cheatsheets.cheatsheets.backgroundwork.async.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.selects.select

/**
 * ============================= Channels =============================
 *
 * The Channel API was added as an inter-coroutine communication
 * primitive.
 *
 * Channel supports any number of senders and receivers, and every
 * value that is sent to a channel is received only once.
 *
 *
 * +-------------+     send      +---------+     receive     +-------------+
 * | producer #1 | ----◉----->  |         |  ----▲●----->   | consumer #1 |
 * +-------------+              |         |                 +-------------+
 *                              | channel |
 * +-------------+ ----▲►-----> |         |  ----►■▲----->   +-------------+
 * | producer #2 |              |         |                 | consumer #2 |
 * +-------------+              +---------+                 +-------------+
 *        ...
 * +-------------+ ----■----->
 * | producer #N |
 * +-------------+
 *
 * Channel types:
 *
 * - Unlimited - channel with capacity Channel.UNLIMITED
 * - Buffered - channel with concrete capacity
 * size or Channel.BUFFERED (which is 64 by default and can be overridden by setting the
 * kotlinx.coroutines.channels.defaultBuffer system
 * property in JVM).
 * - Rendezvous - channel with capacity 0 or
 * Channel.RENDEZVOUS (which is equal to 0), meaning that
 * an exchange can happen only if sender and receiver meet
 * - Conflated - channel with capacity Channel.CONFLATED which
 * has a buffer of size 1, and each new element replaces the previous one.
 */
@OptIn(ExperimentalCoroutinesApi::class)
suspend fun main1() = coroutineScope {
    val channel = produce(
        capacity = Channel.UNLIMITED
    ) {
        repeat(5) { index ->
            println("Producing next one")
            delay(1000)
            send(index * 2)
        }
    }

    channel.consumeEach {
        println(it)
    }
}

/**
 * On buffer overflow
 *
 * • SUSPEND (default) - when the buffer is full, suspend on the send
 * method.
 * • DROP_OLDEST - when the buffer is full, drop the oldest element.
 * • DROP_LATEST - when the buffer is full, drop the latest element.
 *
 * Channel.CONFLATED is the same as setting the capacity to 1 and
 * onBufferOverflow to DROP_OLDEST
 *
 * Currently, the produce function does not allow us to set
 * custom onBufferOverflow, so to set it we need to define a channel
 * using the function Channel
 */
suspend fun main2(): Unit = coroutineScope {
    val channel = Channel<Int>(
        capacity = 2,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    launch {
        repeat(5) { index ->
            channel.send(index * 2)
            delay(100)
            println("Sent")
        }
        channel.close()
    }
    delay(1000)
    for (element in channel) {
        println(element)
        delay(1000)
    }
}

/**
 * We generally use onUndeliveredElement to
 * close resources that are sent by this channel.
 */
val channel = Channel<Int>(2) { int ->
    println(int)
}
// or
// val channel = Channel<Int>(
//      2,
//      onUndeliveredElement = { int ->
//           println(int)
//      }
//  )


/**
 * Fan-out (1 to 3)
 *
 * The elements are distributed fairly. The channel has a FIFO (first-in-first-out) queue of
 * coroutines waiting for an element. This is why in
 * the above example you can see that the elements are received by the
 * next coroutines (0, 1, 2, 0, 1, 2, etc).
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun CoroutineScope.produceNumbers() = produce {
    repeat(10) {
        delay(100)
        send(it)
    }
}
fun CoroutineScope.launchProcessor(
    id: Int,
    channel: ReceiveChannel<Int>
) = launch {
    for (msg in channel) {
        println("#$id received $msg")
    }
}
suspend fun main3(): Unit = coroutineScope {
    val channel = produceNumbers()
    repeat(3) { id ->
        delay(10)
        launchProcessor(id, channel)
    }
}
// #0 received 0
// #1 received 1
// #2 received 2
// #0 received 3
// #1 received 4
// #2 received 5
// #0 received 6
// ...


/**
 * Fan-it (3 to 1)
 */
suspend fun sendString(
    channel: SendChannel<String>,
    text: String,
    time: Long
) {
    while (true) {
        delay(time)
        channel.send(text)
    }
}
fun main4() = runBlocking {
    val channel = Channel<String>()
    launch { sendString(channel, "foo", 200L) }
    launch { sendString(channel, "BAR!", 500L) }
    repeat(50) {
        println(channel.receive())
    }
    coroutineContext.cancelChildren()
}
// (200 ms)
// foo
// (200 ms)
// foo
// (100 ms)
// BAR!
// (100 ms)
// foo
// (200 ms)
// ...


/**
 * Select is a useful function that lets us await the result of the first
 * coroutine that completes, or send to or receive from the first from
 * multiple channels. It is mainly used to implement different patterns
 * for operating on channels, but it can also be used to implement async
 * coroutine races.
 */
fun main(): Unit = runBlocking {
    val c1 = Channel<Char>(capacity = 2)
    val c2 = Channel<Char>(capacity = 2)
// Send values
    launch {
        for (c in 'A'..'H') {
            delay(400)
            select {
                c1.onSend(c) { println("Sent $c to 1") }
                c2.onSend(c) { println("Sent $c to 2") }
            }
        }
    }
// Receive values
    launch {
        while (true) {
            delay(1000)
            val c = select {
                c1.onReceive { "$it from 1" }
                c2.onReceive { "$it from 2" }
            }
            println("Received $c")
        }
    }
}
// Sent A to 1
// Sent B to 1
// Received A from 1
// Sent C to 1
// Sent D to 2
// Received B from 1
// Sent E to 1
// Sent F to 2
// Received C from 1
// Sent G to 1
// Received E from 1
// Sent H to 1
// Received G from 1
// Received H from 1
// Received D from 2
// Received F from 2