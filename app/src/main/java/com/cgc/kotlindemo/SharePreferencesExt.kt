package com.cgc.kotlindemo

import android.content.Context
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * author: CHENGC
 * date:2020/4/12 19:09
 * describe:
 */

class Preference<T>(
    val content: Context,
    val key: String,
    val value: T,
    val preName: String = "default"
) : ReadWriteProperty<Any?, T> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}