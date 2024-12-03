package com.example.kotlindiveinto

class Foo {
    var  b = 0
    fun foo() {
        println("Foo $b")
    }
}

context(Foo)
fun callFoo() {
    b = 25
    foo()
}
fun Foo.callFooExt(){
    foo()
}

fun main() {
    val foo = Foo()

    with(foo) {
        callFoo()
    }

    foo.callFooExt()
}