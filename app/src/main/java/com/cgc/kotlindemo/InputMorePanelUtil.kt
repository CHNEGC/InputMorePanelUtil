package com.cgc.kotlindemo

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_main.*

/**
 * author: CHENGC
 * date:2020/5/3 15:45
 * describe:
 */
class InputMorePanelUtil private constructor(val mActivity: Activity) {


    private var mMorePanelState: Int = MORE_PANEL_STATE_HIND
    private var mCurrentShowMoreInputPanel: Int = -1
    private var mCurrentVoiceClickState: Int = VOICE_SOFT_KEY_BOARD_INPUT
    private var mCurrentMotionClickState: Int = MOTION_SOFT_KEY_BOARD_INPUT
    private var mSoftKeyBoardState: Int = SOFT_KEY_BOARD_STATE_HIND
    private var mCurrentClickState: Int = -1
    private var mSoftKeyBoardHeight: Float = 0f


    //软键盘管理类
    private var mInputManager
            : InputMethodManager? = null
    private var sp: SharedPreferences? = null
    private var mInputEditText: EditText? = null
    private var mMoreBottom: View? = null
    private var mVoiceBottom: View? = null
    private var mVoiceShowView: View? = null
    private var mMotionBottom: View? = null
    private var mContentView: View? = null
    private var mMoreContentView: FrameLayout? = null
    private var mMorePanelFragment: Fragment? = null
    private var mMotionPanelFragment: Fragment? = null
    private var mFragmentManager: FragmentManager? = null
    private var mOnVoiceClickListenerCallBack: ((mVoiceClickState: Int) -> Unit)? = null
    private var mOnMotionClickListenerCallBack: ((mMotionClickState: Int) -> Unit)? = null

    init {
        //监听获取软键盘的高度
        SoftKeyBoardListener.setListener(mActivity, object :
            SoftKeyBoardListener.OnSoftKeyBoardChangeListener {
            override fun keyBoardShow(height: Int) {
                mSoftKeyBoardState = SOFT_KEY_BOARD_STATE_SHOW
                mActivity.view.postDelayed({
                    resetMoreContentPanelHeight(height.toFloat())
                }, DURATION.plus(50))
                mMoreContentView?.layoutParams?.height = height
                saveSoftKeyHeightToCache(sp, height.toFloat())
            }

            override fun keyBoardHide(height: Int) {
                mSoftKeyBoardState = SOFT_KEY_BOARD_STATE_HIND
                mSoftKeyBoardHeight = height.toFloat()
                saveSoftKeyHeightToCache(sp, height.toFloat())
                mMoreContentView?.layoutParams?.height = height
                if (mCurrentClickState == CLICK_HINT_SOFT_KEY_BOARD) {
                    mCurrentClickState = -1
                    hintMorePanel()
                }
            }
        })
    }


    companion object {
        /**软键盘显示*/
        private const val SOFT_KEY_BOARD_STATE_SHOW: Int = 0x001

        /**软键盘隐藏*/
        private const val SOFT_KEY_BOARD_STATE_HIND: Int = 0x002

        /**更多面板隐藏*/
        private const val MORE_PANEL_STATE_HIND: Int = 0x011

        /**更多面板显示*/
        private const val MORE_PANEL_STATE_SHOW: Int = 0x012

        /**操作隐藏软键盘同时隐藏更多面板*/
        private const val CLICK_HINT_SOFT_KEY_BOARD: Int = 0x013

        /**操作隐藏软键盘，但是不隐藏更多面板*/
        private const val CLICK_UN_HINT_SOFT_KEY_BOARD: Int = 0x014

        /**加载的是更多面板*/
        private const val LOAD_SHOW_MORE_INPUT_PANEL: Int = 0x015

        /**加载的是表情面板*/
        private const val LOAD_SHOW_MOTION_INPUT_PANEL: Int = 0x016

        /**软键盘输入*/
        const val VOICE_SOFT_KEY_BOARD_INPUT: Int = 0x017

        /**录语音状态*/
        const val VOICE_INPUT: Int = 0x018

        /**软键盘输入*/
        const val MOTION_SOFT_KEY_BOARD_INPUT: Int = 0x019

        /**录语音状态*/
        const val MOTION_INPUT: Int = 0x020

        /**动画时长*/
        private const val DURATION: Long = 200L

        private const val SHARE_PREFERENCE_NAME: String = "input_more_util"
        private const val SHARE_PREFERENCE_SOFT_INPUT_HEIGHT = "soft_input_height"

        fun with(
            mActivity: Activity,
            mFragmentManager: FragmentManager? = null
        ): InputMorePanelUtil {
            val mInputMorePanelUtil = InputMorePanelUtil(mActivity)
            mInputMorePanelUtil.mFragmentManager = mFragmentManager
            mInputMorePanelUtil.mInputManager =
                mActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            mInputMorePanelUtil.sp =
                mActivity.getSharedPreferences(SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE)
            //获取保存的软键盘高度
            mInputMorePanelUtil.mSoftKeyBoardHeight = getCacheSoftKeyHeight(mInputMorePanelUtil.sp)
            return mInputMorePanelUtil
        }

        /**保存软件盘高度到本地*/
        fun saveSoftKeyHeightToCache(sp: SharedPreferences?, mSoftHeight: Float) {
            //存一份到本地
            if (mSoftHeight > 0) {
                sp?.edit()?.putFloat(
                    SHARE_PREFERENCE_SOFT_INPUT_HEIGHT,
                    mSoftHeight
                )?.apply()
            }
        }

        /**获取本地保存的软键盘高度*/
        private fun getCacheSoftKeyHeight(sp: SharedPreferences?): Float {
            return sp?.getFloat(
                SHARE_PREFERENCE_SOFT_INPUT_HEIGHT,
                741f
            ) ?: 741f
        }
    }

    /**绑定编辑框*/
    @SuppressLint("ClickableViewAccessibility")
    fun bindInputEditText(mInputEditText: EditText?): InputMorePanelUtil {
        this.mInputEditText = mInputEditText
        this.mInputEditText?.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    mCurrentClickState = CLICK_HINT_SOFT_KEY_BOARD
                    when (mSoftKeyBoardState) {
                        SOFT_KEY_BOARD_STATE_HIND -> {
                            //软键盘隐藏状态
                            when (mMorePanelState) {
                                MORE_PANEL_STATE_HIND -> {
                                    //更多面板隐藏状态
                                    morePanelGONE()
                                    showMorePanel()
                                    showSoftInput()
                                }
                                MORE_PANEL_STATE_SHOW -> {
                                    //如果面板已经显示就直接显示软键盘
                                    showSoftInput()
                                }
                            }
                        }
                        SOFT_KEY_BOARD_STATE_SHOW -> {
                            //软键盘显示

                        }
                    }
                }
            }

            return@setOnTouchListener true
        }
        return this
    }

    /**绑定内容容器*/
    @SuppressLint("ClickableViewAccessibility")
    fun bindContentView(mContentView: View?): InputMorePanelUtil {
        this.mContentView = mContentView
        return this
    }

    /**绑定更多面板*/
    fun bindMorePanelView(mMoreContent: FrameLayout?): InputMorePanelUtil {
        this.mMoreContentView = mMoreContent
        this.mMoreContentView?.layoutParams?.height = mSoftKeyBoardHeight.toInt()
        morePanelGONE()
        return this
    }

    /**绑定切换语音录入按钮*/
    fun bindVoiceBottom(
        mVoiceBottom: View,
        mOnVoiceClickListenerCallBack: ((mVoiceClickState: Int) -> Unit)? = null
    ): InputMorePanelUtil {
        this.mVoiceBottom = mVoiceBottom
        this.mOnVoiceClickListenerCallBack = mOnVoiceClickListenerCallBack
        this.mVoiceBottom?.setOnClickListener {
            when (mCurrentVoiceClickState) {
                VOICE_SOFT_KEY_BOARD_INPUT -> {
                    if (mSoftKeyBoardState == SOFT_KEY_BOARD_STATE_SHOW ||
                        mMorePanelState == MORE_PANEL_STATE_SHOW
                    ) {
                        mVoiceBottom.postDelayed({hintMorePanel()
                            hideSoftInput()},100)
                    }
                }
                VOICE_INPUT -> {
                    morePanelGONE()
                    showMorePanel()
                    showSoftInput()
                }
            }
            switchVoiceIcon()
        }
        return this
    }

    /**绑定更多操作面板UI*/
    fun bindMoreInputFragment(mMoreInputFragment: MoreInputFragment): InputMorePanelUtil {
        this.mMorePanelFragment = mMoreInputFragment
        return this
    }

    /**绑定表情操作面板UI*/
    fun bindMotionInputFragment(mMotionInputFragment: MotionInputFragment): InputMorePanelUtil {
        this.mMotionPanelFragment = mMotionInputFragment
        return this
    }

    /**绑定表情按钮*/
    fun bindMotionBottom(
        mMotionBottom: View,
        mOnMotionClickListenerCallBack: ((mMotionClickState: Int) -> Unit)? = null
    ): InputMorePanelUtil {
        this.mMotionBottom = mMotionBottom
        this.mOnMotionClickListenerCallBack = mOnMotionClickListenerCallBack
        this.mMotionBottom?.setOnClickListener { v: View? ->
            mCurrentClickState = CLICK_HINT_SOFT_KEY_BOARD
            mInputEditText?.requestFocus()
            onClick(LOAD_SHOW_MOTION_INPUT_PANEL)
            switchMotionIcon()
        }
        return this
    }

    /**绑定更多按钮*/
    fun bindMoreBottom(mMoreBottom: View?): InputMorePanelUtil {
        this.mMoreBottom = mMoreBottom
        this.mMoreBottom?.setOnClickListener { v: View? ->
            mCurrentClickState = CLICK_HINT_SOFT_KEY_BOARD
            mInputEditText?.clearFocus()
            onClick(LOAD_SHOW_MORE_INPUT_PANEL)
            onDefault()
        }
        return this
    }

    /**按钮操作逻辑*/
    private fun onClick(mSwitchType: Int) {
        when (mSoftKeyBoardState) {
            SOFT_KEY_BOARD_STATE_SHOW -> {//软键盘显示
                mCurrentClickState = CLICK_UN_HINT_SOFT_KEY_BOARD
                switchMorePanel(mSwitchType)//加載更多操作面板
                morePanelVISIBLE()
                hideSoftInput()
            }
            SOFT_KEY_BOARD_STATE_HIND -> {//软键盘没有显示
                when (mMorePanelState) {
                    MORE_PANEL_STATE_SHOW -> {
                        if (mCurrentShowMoreInputPanel != mSwitchType) switchMorePanel(
                            mSwitchType
                        )//加載更多操作面板
                        else
                            showSoftInput()
                    }
                    MORE_PANEL_STATE_HIND -> {
                        switchMorePanel(mSwitchType)//加載更多操作面板
                        morePanelVISIBLE()
                        showMorePanel()
                    }
                }
            }
        }
    }

    /**显示更多面板容器*/
    private fun morePanelVISIBLE() {
        mMoreContentView?.visibility = View.VISIBLE
    }

    /**隐藏更多面板容器*/
    private fun morePanelGONE() {
        mMoreContentView?.visibility = View.GONE
    }

    /**显示更多面板*/
    private fun showMorePanel(startValue: Float = 0f, endValue: Float = -mSoftKeyBoardHeight) {
        mMorePanelState = MORE_PANEL_STATE_SHOW
        performAnimation(startValue, endValue)
    }

    /**隐藏更多面板*/
    private fun hintMorePanel(startValue: Float = -mSoftKeyBoardHeight, endValue: Float = 0f) {
        mMorePanelState = MORE_PANEL_STATE_HIND
        performAnimation(startValue, endValue)
    }

    /**执行动画*/
    private fun performAnimation(startValue: Float, endValue: Float) {
        mContentView?.apply {
            val mValueAnimation: ValueAnimator = ValueAnimator.ofFloat(startValue, endValue)
            mValueAnimation.apply {
                duration = DURATION
                addUpdateListener {
                    val v: Float = it.animatedValue as Float
                    translationY = v
                    mMoreContentView?.translationY = v
                }
                interpolator = LinearInterpolator()
                start()
            }
        }
    }

    /**
     * 编辑框获取焦点，并显示软件盘
     */
    private fun showSoftInput() {
        mInputEditText?.postDelayed({
            mInputEditText?.requestFocus()
            mInputEditText?.post { mInputManager!!.showSoftInput(mInputEditText, 0) }
        }, 10)
    }

    /**
     * 隐藏软件盘
     */
    private fun hideSoftInput() {
        mInputManager!!.hideSoftInputFromWindow(mInputEditText?.windowToken, 0)
    }

    /**防止刚开始获取不到软件盘的高度，再获取到软键盘高度后重新计算，方式显示的UI过高或者过低*/
    private fun resetMoreContentPanelHeight(mSoftKeyHeight: Float) {
        val v: Float = mSoftKeyBoardHeight.minus(mSoftKeyHeight)
        if (v > 0) {
            showMorePanel(-mSoftKeyBoardHeight, -mSoftKeyHeight)
        } else if (v < 0) {
            showMorePanel(mSoftKeyBoardHeight, -mSoftKeyHeight)
        }
        this.mSoftKeyBoardHeight = mSoftKeyHeight
    }

    /**
     * 点击返回键时先隐藏表情布局
     */
    fun interceptBackPress(): Boolean {
        if (mMorePanelState == MORE_PANEL_STATE_SHOW) {
            hintMorePanel()
            return true
        }
        return false
    }

    /**手动隐藏更多面板 供外部调用的方法*/
    fun manualHintMorePanel() {
        if (mMorePanelState == MORE_PANEL_STATE_SHOW) {
            hideSoftInput()
            hintMorePanel()
        }
    }

    /**显示更多面板 供外部调用的方法*/
    fun manualShowMorePanel(isShowSoft: Boolean) {
        if (mMorePanelState == MORE_PANEL_STATE_HIND) {
            showMorePanel()
            if (isShowSoft) {
                showSoftInput()
            }
        }
    }

    /**切換更多容器操作内容面板*/
    private fun switchMorePanel(mSwitchType: Int) {
        if (mCurrentShowMoreInputPanel == mSwitchType) {
            return
        }
        val mFragmentTransaction: FragmentTransaction =
            mFragmentManager?.beginTransaction() ?: return
        when (mSwitchType) {
            LOAD_SHOW_MORE_INPUT_PANEL -> {
                mCurrentShowMoreInputPanel = LOAD_SHOW_MORE_INPUT_PANEL
                mMorePanelFragment?.let {
                    mMoreContentView?.id?.let { it1 ->
                        mFragmentTransaction.replace(
                            it1, it
                        )
                    }
                }
            }
            LOAD_SHOW_MOTION_INPUT_PANEL -> {
                mCurrentShowMoreInputPanel = LOAD_SHOW_MOTION_INPUT_PANEL
                mMotionPanelFragment?.let {
                    mMoreContentView?.id?.let { it1 ->
                        mFragmentTransaction.replace(
                            it1, it
                        )
                    }
                }
            }
        }
        mFragmentTransaction.commitAllowingStateLoss()
    }

    private fun switchVoiceIcon() {
        when (mCurrentVoiceClickState) {
            VOICE_SOFT_KEY_BOARD_INPUT -> {
                mCurrentVoiceClickState = VOICE_INPUT

            }
            VOICE_INPUT -> {
                mCurrentVoiceClickState = VOICE_SOFT_KEY_BOARD_INPUT
            }
        }
        this.mOnVoiceClickListenerCallBack?.apply {
            invoke(mCurrentVoiceClickState)
        }
    }

    private fun switchMotionIcon() {
        when (mCurrentMotionClickState) {
            MOTION_SOFT_KEY_BOARD_INPUT -> {
                mCurrentMotionClickState = MOTION_INPUT
            }
            MOTION_INPUT -> {
                mCurrentMotionClickState = MOTION_SOFT_KEY_BOARD_INPUT
            }
        }
        this.mOnMotionClickListenerCallBack?.apply {
            invoke(mCurrentMotionClickState)
        }
    }

    private fun onDefault() {
        mCurrentMotionClickState = MOTION_SOFT_KEY_BOARD_INPUT
        mCurrentVoiceClickState = VOICE_SOFT_KEY_BOARD_INPUT
        switchVoiceIcon()
        switchMotionIcon()
        mInputEditText?.clearFocus()
    }
}