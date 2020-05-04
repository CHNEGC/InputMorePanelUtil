package com.cgc.kotlindemo

/**
 * author: CHENGC
 * date:2020/3/8 14:09
 * describe:
 */
class SimpleClass(var x: Int, val y: Int) : BaseSimpleClass(), SimpleInf {

    fun y() {
        println("Kotlin 类的学习")
    }

    fun z(): String {
        return "z"
    }

    override fun simpleMethod() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun xxxx(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun nonMethod2() {
        super.nonMethod2()
    }
}

open class BB : BaseSimpleClass() {
    val b = 0
    override fun xxxx(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

class CC : BB() {
    val x = 0
}

fun main() {
    val mSimpleClass = SimpleClass(1, 2)
    mSimpleClass.y()

    val simpleClass = SimpleClass::z

    println("simpleClass${simpleClass}")
    println("simpleClass${SimpleClass::z}")

    val cc = CC()

    if (cc is BB) {
        println("智能类型转换${cc.b}")
    }

}