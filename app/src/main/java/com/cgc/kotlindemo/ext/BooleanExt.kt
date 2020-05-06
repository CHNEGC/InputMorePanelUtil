package com.cgc.kotlindemo.ext

/**
 * Boolean 扩展
 * shige chen on 2020/5/6
 * shigechen@globalsources.com
 */
sealed class BooleanExt<out T> constructor(val boolean: Boolean)

object Otherwise : BooleanExt<Nothing>(true)
class WithData<out T>(val data: T) : BooleanExt<T>(false)

inline fun <T> Boolean.yes(block: () -> T): BooleanExt<T> = when {
    this -> WithData(block())
    else -> Otherwise
}

inline fun <T> Boolean.on(block: () -> T): BooleanExt<T> = when {
    this -> Otherwise
    else -> WithData(block())
}

inline infix fun <T> BooleanExt<T>.otherwise(block: () -> T): T {
    return when (this) {
        is Otherwise -> block()
        is WithData<T> -> this.data
        else -> {
            throw IllegalAccessException()
        }
    }
}

inline operator fun <T> Boolean.invoke(block: () -> T) = yes(block)

