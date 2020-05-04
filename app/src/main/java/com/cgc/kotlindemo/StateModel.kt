package com.cgc.kotlindemo

/**
 * author: CHENGC
 * date:2020/5/2 14:15
 * describe:状态模式
 */

//状态接口
interface TvState {
    fun nextChannel()
    fun prevChannel()
    fun turnUp()
    fun turnDown()
}

//关机状态
class PowerOffState : TvState {
    override fun nextChannel() {
        println("PowerOffState.nextChannel")
    }

    override fun prevChannel() {
        println("PowerOffState.prevChannel")
    }

    override fun turnUp() {
        println("PowerOffState.turnUp")
    }

    override fun turnDown() {
        println("PowerOffState.turnDown")
    }
}

//开机状态
class PowerOnState : TvState {
    override fun nextChannel() {
        println("PowerOnState.nextChannel")
    }

    override fun prevChannel() {
        println("PowerOnState.prevChannel")
    }

    override fun turnUp() {
        println("PowerOnState.turnUp")
    }

    override fun turnDown() {
        println("PowerOnState.turnDown")
    }
}

//电源接口
interface PowerController {
    fun powerOn()
    fun powerOff()
}

class TvController : PowerController {
    private var mTvState: TvState? = null


    private fun setTvState(mTvState: TvState) {
        this.mTvState = mTvState
    }

    override fun powerOn() {
        setTvState(PowerOnState())
        println("TvController.powerOn")
    }

    override fun powerOff() {
        setTvState(PowerOffState())
        println("TvController.powerOff")
    }

    fun nextChannel() {
        mTvState?.let {
            it.nextChannel()
        }
    }

    fun prevChannel() {
        mTvState?.also {
            it.prevChannel()
        }
    }

    fun turnUp() {
        mTvState?.apply {
            turnUp()
        }
    }

    fun turnDown() {
        mTvState?.run {
            turnDown()
        }
    }
}


fun main() {
    val mTvController = TvController()
    mTvController.apply {
        //设置开机状态
        powerOn()
        //下一个频道
        nextChannel()
        //调高音量
        turnUp()
        //设置关机状态
        powerOff()
        //调高音量
        turnUp()
    }
}