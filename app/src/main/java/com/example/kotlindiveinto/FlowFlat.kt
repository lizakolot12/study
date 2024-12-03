package com.example.kotlindiveinto

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Date


suspend fun main(): Unit = coroutineScope {
    val flow = getFlow()
    flow.collect {
        println("1 collect $it")
    }
    flow.collect {
        println("2 collect $it")
    }
}

fun getFlow(): Flow<Int> {
    return listOf(1, 2, 3, 4, 5, 6).asFlow()
    }






