package com.example.cheatsheets

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description


/**
 * +--------------------------------------------------+
 * | runTest                                          |
 * |  +--------------------------------------------+  |
 * |  | TestScope                                  |  |
 * |  |  +--------------------------------------+  |  |
 * |  |  | StandardTestDispatcher               |  |  |
 * |  |  |  +--------------------------------+  |  |  |
 * |  |  |  | TestCoroutineScheduler         |  |  |  |
 * |  |  |  +--------------------------------+  |  |  |
 * |  |  +--------------------------------------+  |  |
 * |  +--------------------------------------------+  |
 * +--------------------------------------------------+
 *
 * StandardTestDispatcher creates TestCoroutineScheduler by default
 *
 * StandardTestDispatcher. Unlike most dispatchers, it is not used
 * just to decide on which thread a coroutine should run. Coroutines
 * started with such a dispatcher will not run until we advance virtual
 * time
 */
//@OptIn(ExperimentalCoroutinesApi::class)
//fun standardTestDispatcher() {
//    val dispatcher = StandardTestDispatcher()
//    CoroutineScope(dispatcher).launch {
//        println("Some work 1")
//        delay(1000)
//        println("Some work 2")
//        delay(1000)
//        println("Coroutine done")
//    }
//    println("[${dispatcher.scheduler.currentTime}] Before")
//    dispatcher.scheduler.advanceUntilIdle()
//    println("[${dispatcher.scheduler.currentTime}] After")
//}
// [0] Before
// Some work 1
// Some work 2
// Coroutine done
// [2000] After


/**
 * TestScope does the same (and it collects all exceptions with
 * CoroutineExceptionHandler)
 */
//@OptIn(ExperimentalCoroutinesApi::class)
//fun testScope() {
//    val scope = TestScope()
//    scope.launch {
//        delay(1000)
//        println("First done")
//        delay(1000)
//        println("Coroutine done")
//    }
//    println("[${scope.currentTime}] Before") // [0] Before
//    scope.advanceTimeBy(1000)
//    scope.runCurrent() // First done
//    println("[${scope.currentTime}] Middle") // [1000] Middle
//    scope.advanceUntilIdle() // Coroutine done
//    println("[${scope.currentTime}] After") // [2000] After
//}



//class TestTest {

    /**
     * This is similar to runBlocking but it will immediately progress past delays and into
     * launch and async blocks. You can use this to write tests that execute in the presence
     * of calls to delay without causing your test to take extra time.
     */
//    @Test
//    fun exampleTest() = runBlockingTest {
//        val deferred = async {
//            delay(1_000)
//            async {
//                delay(1_000)
//            }.await()
//        }
//
//        deferred.await() // result available immediately
//    } Deprecated (with error) !!!

//    @Test
//    fun test1() = runTest {
//        assertEquals(0, currentTime)
//        delay(1000)
//        assertEquals(1000, currentTime)
//    }
//    @Test
//    fun test2() = runTest {
//        assertEquals(0, currentTime)
//        coroutineScope {
//            launch { delay(1000) }
//            launch { delay(1500) }
//            launch { delay(2000) }
//        }
//        assertEquals(2000, currentTime)
//    }
//
//    @Test
//    fun test3() = runTest {
//        var i = 0
//        backgroundScope.launch {
//            // This is a scope
//            // that also operates on virtual time, but runTest will not await its
//            // completion
//            while (true) {
//                delay(1000)
//                i++
//            }
//        }
//        delay(1001)
//        assertEquals(1, i)
//        delay(1000)
//        assertEquals(2, i)
//    }
//
//
//}

/**
 * Good way to test ViewModel !!!
 *
 * After setting the Main dispatcher this way, onCreate coroutines will
 * be running on testDispatcher, so we can control their time. We can
 * use the advanceTimeBy function to pretend that a certain amount of
 * time has passed. We can also use advanceUntilIdle to resume all
 * coroutines until they are done.
 */
//@OptIn(ExperimentalCoroutinesApi::class)
//class Test2() {
//
//    private lateinit var scheduler: TestCoroutineScheduler
//    // private lateinit var viewModel: MainViewModel
//
//    @Before
//    fun setUp() {
//        scheduler = TestCoroutineScheduler()
//        Dispatchers.setMain(StandardTestDispatcher(scheduler))
//    }
//
//    @After
//    fun tearDown() {
//        Dispatchers.resetMain()
//        // viewModel.onCleared()
//    }
//}
//
//@ExperimentalCoroutinesApi
//class MainCoroutineRule : TestWatcher() {
//    lateinit var scheduler: TestCoroutineScheduler
//        private set
//    lateinit var dispatcher: TestDispatcher
//        private set
//    override fun starting(description: Description) {
//        scheduler = TestCoroutineScheduler()
//        dispatcher = StandardTestDispatcher(scheduler)
//        Dispatchers.setMain(dispatcher)
//    }
//    override fun finished(description: Description) {
//        Dispatchers.resetMain()
//    }
//}

// @get:Rule
// var mainCoroutineRule = MainCoroutineRule()