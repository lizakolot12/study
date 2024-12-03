package com.example.kotlindiveinto

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main(): Unit = runBlocking {
    val cores = Runtime.getRuntime().availableProcessors()
    val dispatcher = Dispatchers.IO.limitedParallelism(256)

    val test = measureTimeMillis {
        coroutineScope {
            repeat(256) {
                launch(dispatcher) {
                    Thread.sleep(1000)
                }
            }
        }
    }
    println("$test")

}
