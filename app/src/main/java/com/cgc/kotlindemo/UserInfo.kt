package com.cgc.kotlindemo

/**
 * author: CHENGC
 * date:2020/3/27 23:00
 * describe:
 */
//柱构造器
class UserInfo(val name: String) {
    val firstProperty = "First property: $name".also(::println)

    init {
        println("First initializer block that prints ${name}")
    }

    val secondProperty = "Second property: ${name.length}".also(::println)

    init {
        println("Second initializer block that prints ${name.length}")
    }

    //次构造器
    constructor(name: String, age: Int, title: String) : this(name) {
        println("constructor ${name.length}")
    }
}

open class BaseInfo {
    open fun a(){}
}

class OnePersonInfo : BaseInfo() {
    override fun a() {

    }
}


fun main() {
    val userInfo = UserInfo("ShigeChen", 3, "3333")
}