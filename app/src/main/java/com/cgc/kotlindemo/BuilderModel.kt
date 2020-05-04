package com.cgc.kotlindemo

import android.view.Display

/**
 * author: CHENGC
 * date:2020/5/1 23:50
 * describe:
 */
abstract class Computer {
    protected var mBorad: String = ""
    protected var mDisplay: String = ""
    protected var mOS: String = ""

    //设置主板
    fun setBoard(mBoard: String) {
        this.mBorad = mBoard
    }

    //设置显示器
    fun setDisplay(mDisplay: String) {
        this.mDisplay = mDisplay
    }

    //设置操作系统
    abstract fun setOS();

    override fun toString(): String {
        return "Computer Borad=$mBorad, Display=$mDisplay,mOS=$mOS"
    }
}

class MackBook : Computer() {
    override fun setOS() {
        this.mOS = "Mac OS X 10.10"
    }
}

//抽象的builder 类
abstract class Builder {
    //设置主板
    abstract fun builderBoard(mBoard: String)

    //设置显示器
    abstract fun builderDisplay(mDisplay: String)

    //设置操作系统
    abstract fun builderOS()

    //创建电脑
    abstract fun createComputer(): Computer
}

class MacBoolBuilder : Builder() {
    var mComputer: Computer = MackBook()

    override fun builderBoard(mBoard: String) {
        mComputer.setBoard(mBoard)
    }

    override fun builderDisplay(mDisplay: String) {
        mComputer.setDisplay(mDisplay)
    }

    override fun builderOS() {
        mComputer.setOS()
    }

    override fun createComputer(): Computer = mComputer
}

class Director(mBuilder: Builder) {
    val mBuilder: Builder = mBuilder

    fun construct(mBoard: String, mDisplay: String) {
        mBuilder.apply {
            builderBoard(mBoard)
            builderDisplay(mDisplay)
            builderOS()
        }
    }
}

fun main() {
    val builder: Builder = MacBoolBuilder()
    val director = Director(builder)
    director.construct("Apple System", "4K 高清显示屏")


    println("Computer Info:${builder.createComputer().toString()}")

}