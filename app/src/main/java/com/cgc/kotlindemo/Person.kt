package com.cgc.kotlindemo

/**
 * author: CHENGC
 * date:2020/3/15 15:28
 * describe:
 */
class Person(var age: Int, var name: String) {
    override fun equals(other: Any?): Boolean {
        //判断other 是否能转成Person，如果可以就返回一个person对象。否则就返回null
        //如果是返回null就返回false
        val other = (other as? Person) ?: return false
        //==比较的是值是否相等
        return other.age == this.age && other.name == this.name
    }

    override fun hashCode(): Int {
        return 1 + 7 * age + 13 * name.hashCode()
    }
}

fun main() {
    val person = HashSet<Person>()

    (0..5).forEach { person += Person(20, "CHENGC") }

    println(person.size)
}

