package com.example.kotlindiveinto

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch


class DataService {
    private var callback: ((Int) -> Unit)? = null

    fun setCallback(listener: (Int) -> Unit) {
        callback = listener
    }

    fun simulateDataUpdate(newValue: Int) {
        callback?.invoke(newValue)
    }

    fun removeCallback() {
        callback = null
    }
}

fun getDataUpdates(dataService: DataService) = callbackFlow {
    val callback: (Int) -> Unit = { value ->
        val result = trySend(value)
        println(result.isSuccess)
    }

    dataService.setCallback(callback)
    awaitClose {
        println("remove callback")
        dataService.removeCallback()
    }
}.buffer(2, onBufferOverflow = BufferOverflow.SUSPEND)

suspend fun main(): Unit = coroutineScope {
    val dataService = DataService()

    val updatesFlow = getDataUpdates(dataService)

    launch {
        updatesFlow.collect { data ->
            delay(1000)
            println("Data updated: $data")
        }
    }

    launch {
        for (i in 1..5) {
            delay(30)
            println("Send $i")
            dataService.simulateDataUpdate(i)
        }
    }
}





