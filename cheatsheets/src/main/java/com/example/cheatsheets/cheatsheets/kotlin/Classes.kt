package com.example.cheatsheets.cheatsheets.kotlin

sealed class Seal {
    abstract val x: Int

    class FirstSeal(
        override val x: Int = 3
    ) : Seal()
}

sealed interface Inter { // decompiles to interface

    fun cvx() = println()

    companion object {
        fun create() = println()
    }
}

