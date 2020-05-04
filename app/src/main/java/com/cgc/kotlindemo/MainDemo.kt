package com.cgc.kotlindemo

/**
 * author: CHENGC
 * date:2020/3/7 1:12
 * describe:
 */
class MainDemo {
}

class Foo {}

fun foo() {
    println("foo")
}

class Foob {
    fun bar(p0: String, p1: String) {}
}


fun String.isEmail(): Boolean {
    return false
}

fun main(args: Array<String>) {

    var nonNull: String? = "Hellop"

    nonNull = null

    var length = nonNull?.length ?: 0


    val foo = ::foo
    val foob = Foob::bar


    //只读变量
    val a = 1
    //可读写变量
    var r = "Hello Kotlin"


    fun yy(
        p: (Foo, String, Long) -> Any
    ) {
    }
    println("不可改变的变量a=$a")
    println("可读写的变量r:$r")
    r = "shige chen"
    println("改变了r:$r")

    val l = 1L
    val d = 1.0
    val f = 1f

    val d1: Double = f.toDouble()

    val str = "Hello Word"
    val str1 = "Hello"
    val str2 = str

    println(str == str1)
    println(str == str2)
    println(str1 == str2)

    //==比较字符串内容是否相同,===比较引用
    println(str === str1)

    val intArray = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
    val intArray2 = IntArray(10) { it + 1 }
    arrayOf("2", "3")
    arrayOf(1, 2, 3, 4)

    println(intArray.contentToString())
    println(intArray2.contentToString())

    println(intArray[0])

    //数组遍历
    for (it in intArray) {
        println("数组遍历：$it")
    }

    intArray.forEach { println(it) }

    if (1 in intArray) {
        println("存在")
    }

    if (10 !in intArray) {
        println("不存在")
    }

    //区间
    val intRangeStr = 1..10
    val intRangeString = 1..10 step 2

    val intRangExcl = 1 until 10//[1,10)
    val intRangExcl2 = 1 until 10 step 3//[1,10)

    val intRange3 = 10 downTo 1
    val intRange4 = 10 downTo 1 step 3

    println(intRangeStr.joinToString())
    println(intRangeString.joinToString())

    for (element in intRangeStr) {
        println(element)
    }
    intRangeStr.forEach { println(it) }

    if (2 in intRangeStr) {

    }
    if (2 !in intRangeStr) {

    }


    for (i in intArray.indices) {

    }

    //集合
    //不可变的List
    val list: List<String> = listOf("1", "2", "2", "2", "2", "2", "2")
    //可变的list
    val mutableList: MutableList<String> = mutableListOf("1", "3", "4")
    val map: Map<String, String> = mapOf("1" to "1", "2" to "2")
    val mutableListMap: MutableMap<String, String> = mutableMapOf("1" to "1", "2" to "2")


    for (str in list) {
        println("不可变集合遍历：$str")
    }

    list.forEach { s: String -> print(s) }


    val stringList = ArrayList<String>()

    mutableList += "2"

    mutableList[1] = "2"
    val string = mutableList[2]

    mutableListMap["1"] = "2"
    val mapStr = mutableListMap["2"]

    val pair = "1" to "2"
    val pair2 = Pair("1", "2")
    val first = pair.first
    val second = pair.second
    val (x, y) = pair

    val mutableMapPair: MutableMap<String, String> = mutableMapOf(pair, pair)


    val triple = Triple(1, 1, 1)


}
