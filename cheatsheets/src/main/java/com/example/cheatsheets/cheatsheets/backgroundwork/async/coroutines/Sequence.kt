package com.example.cheatsheets.cheatsheets.backgroundwork.async.coroutines

import java.math.BigInteger

/**
 * A Kotlin sequence is a similar concept to a collection (like List or
 * Set), but it is evaluated lazily, meaning the next element is always
 * calculated on demand, when it is needed
 *
 * Each number is generated on demand,
 * not in advance.
 */
val fibonacci: Sequence<BigInteger> = sequence {
    var first = 0.toBigInteger()
    var second = 1.toBigInteger()
    while (true) {
        yield(first)
        val temp = first
        first += second
        second = temp
    }
}

fun main() {
    print(fibonacci.take(10).toList())
}
// [0, 1, 1, 2, 3, 5, 8, 13, 21, 34]
