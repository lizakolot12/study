package com.example.kotlindiveinto

import kotlin.random.Random

fun main2() {
    var value: String?
    value = "something"
    val l = Random.nextInt()
    try {
        if (l % 2 == 0) {
            value = null
        } else {
            value = "two"
        }
        if(l % 2 == 0) {
            throw Exception("D")
        }
    } catch (ex: Exception) {
        println("${value.length}")
    }
    println("${value?.length}")
}
interface Status {
    fun signal() {}
}



