package com.example.cheatsheets.cheatsheets.androidsdk

import android.os.Environment
import android.os.Looper
import android.os.Process
import android.os.Trace
import android.os.UserHandle
import android.util.Log
import android.util.LogPrinter
import java.io.File

/**
 * Whole app starts at [ActivityThread]. Like any java program it has
 * public static void main(String[] args)
 */
fun main(args: Array<String>) {
    // . . .

    Looper.prepareMainLooper() // Here is initialization of main looper

    // . . .

//    if (android.app.ActivityThread.sMainThreadHandler == null) {
//        android.app.ActivityThread.sMainThreadHandler = thread.getHandler()
//    }

    Looper.loop()

    throw RuntimeException("Main thread loop unexpectedly exited")
}