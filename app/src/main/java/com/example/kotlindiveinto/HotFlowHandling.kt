package com.example.kotlindiveinto

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

fun createColdFlow1(): Flow<Int> {
    return flow {
        for (i in 1..3) {
            println("---emit $i")
            emit(i)
            delay(1000)
        }
    }
}

@OptIn(ExperimentalStdlibApi::class)
fun main(): Unit = runBlocking {
    println("Dispatcher main: ${coroutineContext[CoroutineDispatcher]}")
 withContext(Dispatchers.IO) {
        val singleValue = createColdFlow1() // will be executed on IO if context wasn't specified before
            .map {
                println("Dispatcher map: ${coroutineContext[CoroutineDispatcher]}")
                it.toString()
            }
            .flowOn(Dispatchers.IO)
            .filter {
                println("Dispatcher filter: ${coroutineContext[CoroutineDispatcher]}")
                it != "" } // Will be executed in Default
            .flowOn(Dispatchers.Default)
            .collect{
                println("Dispatcher collect: ${coroutineContext[CoroutineDispatcher]}")
                println(it)
            } // Will be executed in the Main
    }
}





