package com.cgc.kotlindemo

/**
 * author: CHENGC
 * date:2020/5/2 14:55
 * describe:
 */
interface UserState {
    /**转发*/
    fun forward()

    /**评论*/
    fun comment()
}

/**已登录状态*/
class LoginState : UserState {
    override fun forward() {
        println("LoginState.forward")
    }

    override fun comment() {
        println("LoginState.comment")
    }
}

/**退出状态*/
class LoginOut : UserState {
    override fun forward() {
        println("LoginOut.forward")
    }

    override fun comment() {
        println("LoginOut.comment")
    }
}

object LoginContext {
    //默认为未登录状态
   private var mUserState: UserState? = LoginOut()

    fun setLoginState(mLoginState: LoginState) {
        this.mUserState = mLoginState
    }

    fun setLoginOutState(mLoginOut: LoginOut) {
        this.mUserState = mLoginOut
    }

    fun forward() {
        mUserState?.forward()
    }

    fun comment() {
        mUserState?.comment()
    }
}

fun main() {
    LoginContext.setLoginState(LoginState())
    LoginContext.forward()
    LoginContext.comment()

    LoginContext.setLoginOutState(LoginOut())
    LoginContext.forward()
    LoginContext.comment()
}