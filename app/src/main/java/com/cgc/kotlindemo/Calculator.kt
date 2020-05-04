package com.cgc.kotlindemo

/**
 * author: CHENGC
 * date:2020/3/8 11:26
 * describe:
 */

fun main(vararg args: String) {

    //判断数组是否满足条件
    if ((args.size < 3)) {
        return showHelp()
    }

    //取出中间字符
    val operation = mapOf(
        "+" to ::plus,
        "-" to ::less,
        "*" to ::multiply,
        "/" to ::except
    )
    val op = args[1]
    //判断是否为空，如果为空的话就执行：后面的语句
    val opFunc = operation[op] ?: return showHelp()


    try {
        println("输出结果：${args.joinToString(" ")}")
        println("Output:${opFunc(args[0].toInt(), args[2].toInt())}")
        println("====================使用invoke===========================")
        println("Output:${opFunc.invoke(args[0].toInt(), args[2].toInt())}")
    } catch (e: Exception) {
        showHelp()
    }

    val p = ::plus

    p(12, 33)
    val re = ::plus.invoke(22, 33)
}

fun plus(value1: Int, value2: Int): Int {
    return value1 + value2
}

fun less(value1: Int, value2: Int): Int {
    return value1 - value2
}

fun multiply(value1: Int, value2: Int): Int {
    return value1 * value2
}

fun except(value1: Int, value2: Int): Int {
    return value1 / value2
}


fun showHelp() {
    println(
        """计算器只能接受
        输入3*4这样的格式
        输入结果12
    """.trimMargin()
    )
}