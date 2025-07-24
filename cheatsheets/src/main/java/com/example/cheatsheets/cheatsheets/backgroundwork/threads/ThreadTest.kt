package com.example.cheatsheets.cheatsheets.backgroundwork.threads

import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.thread

/**
 * Запуск потока
 */
fun main() {

    var x = 1
    var x1 = Object()

    val r1 = Runnable {
        repeat(5) {
            if (!Thread.currentThread().isInterrupted) {
                x -= 1
                println(x)
            }
        }
    }

    val t = Thread(r1)
    t.start()
    t.interrupt()


    class MyThread : Thread() {
        override fun run() {
            println("Running in custom thread!")
        }
    }

    /**
     * котлин стайл, сразу запускает
     */
    thread {  }

    /**
     * джава стайл, синтаксический сахар для:
     *
     * val t = Thread(Runnable {
     *     println("Kotlin way")
     * })
     * + надо стартовать вручную
     */
    Thread { }
}

/**
 * Механизмы синхронизации
 *
 * 1. ReentrantLock class - это альтернатива synchronized,
 * реализованная на Java-уровне с помощью очереди и флага,
 * которая не использует встроенные мониторы JVM,
 * но позволяет вручную делать всё то же самое, и даже больше.
 *
 * Обязательно освобождать в finally
 *
 *
 * Далее все механизмы используют монитор объекта -
 *
 * Монитор объекта — это внутренний механизм JVM, встроенный в каждый объект,
 * который обеспечивает взаимоисключение (mutual exclusion) и ожидание условий (wait/notify).
 *
 * Монитор активируется при использовании ключевого слова synchronized и реализуется через intrinsic lock объекта.
 *
 * Только один поток может одновременно захватить монитор объекта, остальные потоки блокируются, пока он не будет освобождён
 *
 * ->
 *
 * 2. @Synchronized fun method() == public synchronized void method()
 * По сути делает просто
 *
 * fun doSomething() {
 *     synchronized(this) {
 *         // ...
 *     }
 * }
 * Т.е. захватывает монитор своего объекта
 *
 * 3.
 * val lock = Any()
 * synchronized(lock) {
 *     } - захват монитора чужого объекта. Потому что монитор (intrinsic lock) — это не приватная штука,
 * а общедоступный механизм синхронизации, встроенный в каждый объект.
 * Любой поток имеет право синхронизироваться на любом объекте, к которому у него есть доступ.
 */
class SyncTests() {

    /**
     * Reentrant lock
     */
    private val myLock1 = ReentrantLock()

    /**
     * Object used for its monitor
     */
    private val myLock2 = Any()

    fun first() {

        myLock1.lock(); // a ReentrantLock object
        try
        {
            // critical section
        }
        finally
        {
            myLock1.unlock(); // make sure the lock is unlocked even if an exception is thrown
        }

    }

    @Synchronized
    fun second() {
        // critical section
    }

    fun third() {
        synchronized(myLock2) {
            // critical section
        }
    }
}