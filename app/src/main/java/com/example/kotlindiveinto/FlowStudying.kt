package com.example.kotlindiveinto

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Date
import java.util.Random


fun main(): Unit = runBlocking {
    var i = 0
    launch {
        coldFlow()
            .collectLatest {
                i++
                if (i % 2 == 0) {
                    delay(1000)
                } else {
                    delay(2000)
                }
                println("collect " + Date().time / 1000 + "  $it")
            }
    }
}


suspend fun hotFlowChecking() = coroutineScope {
    val hotFlow = hotFlow(this)


    launch {
        repeat(5) {
            /*   hotFlow.update {
                   Date().time.toString()
               }*/
            delay(1000)
        }
    }


    launch {
        val secodnd = hotFlow.shareIn(this, SharingStarted.Eagerly, replay = 5)
        delay(3000)
        secodnd.collect { value ->
            println("Collector 1 -> $value")
        }
    }


    launch {
        hotFlow.collect { value ->
            println("Collector 2 -> $value")
        }
    }
}

fun hotFlow(scope: CoroutineScope): Flow<Long> {
    return coldFlow().shareIn(scope, SharingStarted.Eagerly, replay = 1)
    // return MutableStateFlow(Date().time.toString())
}


fun coldFlow(): Flow<Long> {
    return flow {
        for (i in 1..5) {
            delay(1000)
            println("---emit " + Date().time / 1000 + "  $i")
            emit(i.toLong())
        }
    }
}




