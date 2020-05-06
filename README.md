# InputMorePanelUtil
仿微信软键盘弹出效果

最近做公司的项目接手到了IM聊天这块的模块，发现和软件盘的交互的体验不是很好，就想这去优化一下。
想了很久都没有什么头绪，后来就研修了一下微信的软件盘的交互，就想到仿微信实现一个优化一下。

1.思路

![image](https://github.com/CHNEGC/InputMorePanelUtil/blob/master/images/Snipaste_2020-05-06_11-50-54.png)

UI代码：


<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <LinearLayout
        android:id="@+id/view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="#fff000"
        android:orientation="vertical">

        <ListView
            android:id="@+id/listView"
            android:layout_width="match_parent"
            android:layout_height="0dp"
            android:layout_weight="1" />

        <androidx.constraintlayout.widget.ConstraintLayout
            android:layout_width="match_parent"
            android:layout_height="50dp"
            android:animateLayoutChanges="true"
            android:orientation="horizontal"
            android:padding="5dp">

            <Button
                android:id="@+id/btnSwitchVoice"
                android:layout_width="60dp"
                android:layout_height="wrap_content"
                android:text="语音"
                app:layout_constrainedWidth="true"
                app:layout_constraintEnd_toStartOf="@id/et"
                app:layout_constraintHorizontal_bias="0"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent" />

            <EditText
                android:id="@+id/et"
                android:layout_width="0dp"
                android:layout_height="match_parent"
                android:background="#ffffff"
                app:layout_constraintEnd_toStartOf="@id/btnEmotion"
                app:layout_constraintStart_toEndOf="@id/btnSwitchVoice"
                app:layout_constraintTop_toTopOf="parent" />

            <TextView
                android:id="@+id/tvLongVoice"
                android:layout_width="0dp"
                android:layout_height="match_parent"
                android:background="#FFFFFF"
                android:gravity="center"
                android:text="长按录音"
                android:textColor="#999999"
                android:textSize="18sp"
                android:visibility="gone"
                app:layout_constraintEnd_toStartOf="@id/btnEmotion"
                app:layout_constraintStart_toEndOf="@id/btnSwitchVoice"
                app:layout_constraintTop_toTopOf="parent"
                tools:visibility="visible" />

            <Button
                android:id="@+id/btnEmotion"
                android:layout_width="60dp"
                android:layout_height="wrap_content"
                android:text="表情"
                app:layout_constraintEnd_toStartOf="@id/btnMore"
                app:layout_constraintStart_toEndOf="@id/et"
                app:layout_constraintTop_toTopOf="parent" />

            <Button
                android:id="@+id/btnMore"
                android:layout_width="60dp"
                android:layout_height="wrap_content"
                android:text="Login"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintTop_toTopOf="parent" />
        </androidx.constraintlayout.widget.ConstraintLayout>

        <View
            android:layout_width="match_parent"
            android:layout_height="50dp"
            android:background="#ff9999" />
    </LinearLayout>

    <FrameLayout
        android:id="@+id/view2"
        android:layout_width="match_parent"
        android:layout_height="100dp"
        android:background="#ff0000"
        app:layout_constraintTop_toBottomOf="@id/view" />
</androidx.constraintlayout.widget.ConstraintLayout>


引用：



        mInputMorePanelUtil = InputMorePanelUtil.with(this, supportFragmentManager).apply {
            bindContentView(mView)
            bindInputEditText(mEditText)
            bindMorePanelView(mView2)
            bindListView(mListView)
            bindMotionBottom(findViewById(R.id.btnEmotion)) {
                when (it) {
                    MOTION_SOFT_KEY_BOARD_INPUT -> {
                        //软键盘输入状态
                        btnEmotion.text = "表情"
                    }
                    MOTION_INPUT -> {
                        //录音状态
                        btnEmotion.text = "键盘"
                    }
                }
            }
            bindVoiceBottom(findViewById(R.id.btnSwitchVoice)) {
                when (it) {
                    VOICE_SOFT_KEY_BOARD_INPUT -> {
                        //软键盘输入状态
                        tvLongVoice.visibility = View.GONE
                        mEditText.visibility = View.VISIBLE
                        btnSwitchVoice.text = "语音"
                    }
                    VOICE_INPUT -> {
                        //录音状态
                        tvLongVoice.visibility = View.VISIBLE
                        mEditText.visibility = View.INVISIBLE
                        btnSwitchVoice.text = "键盘"
                    }
                }
            }
            bindMoreBottom(findViewById(R.id.btnMore))
            bindMoreInputFragment(MoreInputFragment.newInstance()!!)
            bindMotionInputFragment(MotionInputFragment.newInstance()!!)
        }
        
       
 工具类代码：
 
 
 
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
import android.widget.AbsListView
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ListView
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.cgc.kotlindemo.ext.otherwise
import com.cgc.kotlindemo.ext.yes
import kotlinx.android.synthetic.main.activity_main.*

/**
 * author: CHENGC
 * date:2020/5/3 15:45
 * describe:
 */
 
 
class InputMorePanelUtil private constructor(val mActivity: Activity) 
{
    private var mMorePanelState: Int = MORE_PANEL_STATE_HIND
    private var mCurrentShowMoreInputPanel: Int = -1
    private var mCurrentVoiceClickState: Int = VOICE_SOFT_KEY_BOARD_INPUT
    private var mCurrentMotionClickState: Int = MOTION_SOFT_KEY_BOARD_INPUT
    private var mSoftKeyBoardState: Int = SOFT_KEY_BOARD_STATE_HIND
    private var mCurrentClickState: Int = -1
    private var mSoftKeyBoardHeight: Float = 0f
    private var mOldTimeLong: Long = 0


    //软键盘管理类
    private var mInputManager
            : InputMethodManager? = null
    private var sp: SharedPreferences? = null
    private var mInputEditText: EditText? = null
    private var mMoreBottom: View? = null
    private var mVoiceBottom: View? = null
    private var mMotionBottom: View? = null
    private var mContentView: View? = null
    private var mMoreContentView: FrameLayout? = null
    private var mMorePanelFragment: Fragment? = null
    private var mMotionPanelFragment: Fragment? = null
    private var mFragmentManager: FragmentManager? = null
    private var mListView: ListView? = null
    private var mOnVoiceClickListenerCallBack: ((mVoiceClickState: Int) -> Unit)? = null
    private var mOnMotionClickListenerCallBack: ((mMotionClickState: Int) -> Unit)? = null

    private constructor(mActivity: Activity, mFragmentManager: FragmentManager? = null) : this(
        mActivity
    ) {
        this.mFragmentManager = mFragmentManager
    }

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
                saveSoftKeyHeightToCache(height.toFloat())
            }

            override fun keyBoardHide(height: Int) {
                mSoftKeyBoardState = SOFT_KEY_BOARD_STATE_HIND
                mSoftKeyBoardHeight = height.toFloat()
                saveSoftKeyHeightToCache(height.toFloat())
                mMoreContentView?.layoutParams?.height = height
                if (mCurrentClickState == CLICK_HINT_SOFT_KEY_BOARD) {
                    mCurrentClickState = -1
                    hintMorePanel()
                }
            }
        })

        mInputManager =
            mActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        sp = mActivity.getSharedPreferences(SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE)
        //获取保存的软键盘高度
        mSoftKeyBoardHeight = getCacheSoftKeyHeight()
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

        /**软键盘默认高度*/
        const val DEFAULT_SOFT_KEY_BOARD_HEIGHT: Float = 741f

        private const val SHARE_PREFERENCE_NAME: String = "input_more_util"
        private const val SHARE_PREFERENCE_SOFT_INPUT_HEIGHT = "soft_input_height"

        fun with(
            @NonNull
            mActivity: Activity,
            mFragmentManager: FragmentManager? = null
        ): InputMorePanelUtil {
            return InputMorePanelUtil(mActivity, mFragmentManager)
        }
    }

    /**保存软件盘高度到本地*/
    private fun saveSoftKeyHeightToCache(mSoftHeight: Float) {
        //存一份到本地
        if (mSoftHeight > 0) {
            sp?.edit()?.putFloat(
                SHARE_PREFERENCE_SOFT_INPUT_HEIGHT,
                mSoftHeight
            )?.apply()
        }
    }

    /**获取本地保存的软键盘高度*/
    private fun getCacheSoftKeyHeight(): Float {
        return sp?.getFloat(
            SHARE_PREFERENCE_SOFT_INPUT_HEIGHT,
            DEFAULT_SOFT_KEY_BOARD_HEIGHT
        ) ?: DEFAULT_SOFT_KEY_BOARD_HEIGHT
    }

    /**绑定编辑框*/
    @SuppressLint("ClickableViewAccessibility")
    fun bindInputEditText(mInputEditText: EditText?): InputMorePanelUtil {
        this.mInputEditText = mInputEditText
        this.mInputEditText?.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    isNotDoubleClick().yes {
                        isScroll().yes { mInputEditText?.postDelayed({ inputEditClick() }, 200) }
                            .otherwise {
                                inputEditClick()
                            }
                        return@setOnTouchListener true
                    }
                }
            }

            return@setOnTouchListener true
        }
        return this
    }

    /**编辑框点击*/
    private fun inputEditClick() {
        mCurrentClickState = CLICK_HINT_SOFT_KEY_BOARD
        when (mSoftKeyBoardState) {
            SOFT_KEY_BOARD_STATE_HIND -> {
                //软键盘隐藏状态
                when (mMorePanelState) {
                    MORE_PANEL_STATE_HIND -> {
                        //更多面板隐藏状态
                        morePanelGONE()
                        showMorePanel()
                        mVoiceBottom?.postDelayed({
                            showSoftInput()
                        }, 50)
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

    /**绑定列表*/
    @SuppressLint("ClickableViewAccessibility")
    fun bindListView(mListView: ListView?): InputMorePanelUtil {
        this.mListView = mListView
        this.mListView?.setOnTouchListener(View.OnTouchListener { v, event ->
            manualHintMorePanel()
            return@OnTouchListener false
        })
        return this
    }

    /**绑定切换语音录入按钮*/
    fun bindVoiceBottom(
        mVoiceBottom: View?,
        mOnVoiceClickListenerCallBack: ((mVoiceClickState: Int) -> Unit)? = null
    ): InputMorePanelUtil {
        this.mVoiceBottom = mVoiceBottom
        this.mOnVoiceClickListenerCallBack = mOnVoiceClickListenerCallBack
        this.mVoiceBottom?.setOnClickListener {
            isNotDoubleClick().yes {
                isScroll().yes {
                    mVoiceBottom?.postDelayed({ voiceClick() }, 200)
                }.otherwise {
                    voiceClick()
                }
            }
        }
        return this
    }


    /**语音点击*/
    private fun voiceClick() {
        switchVoiceIcon()
        when (mCurrentVoiceClickState) {
            VOICE_SOFT_KEY_BOARD_INPUT -> {
                morePanelGONE()
                showMorePanel()
                mVoiceBottom?.postDelayed({
                    showSoftInput()
                }, 50)
            }
            VOICE_INPUT -> {
                (mSoftKeyBoardState == SOFT_KEY_BOARD_STATE_SHOW ||
                        mMorePanelState == MORE_PANEL_STATE_SHOW
                        ).yes {
                        hideSoftInput()
                        mVoiceBottom?.postDelayed({
                            hintMorePanel()
                        }, 50)
                    }
            }
        }
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
            isNotDoubleClick().yes {
                isScroll().yes {
                    mMotionBottom.postDelayed({
                        motionClick()
                    }, 200)
                }.otherwise {
                    motionClick()
                }
            }
        }
        return this
    }

    /**表情按钮点击*/
    private fun motionClick() {
        mCurrentClickState = CLICK_HINT_SOFT_KEY_BOARD
        mCurrentVoiceClickState = VOICE_INPUT
        switchVoiceIcon()
        onClick(LOAD_SHOW_MOTION_INPUT_PANEL)
        switchMotionIcon()
        mInputEditText?.requestFocus()
    }

    /**绑定更多按钮*/
    fun bindMoreBottom(mMoreBottom: View?): InputMorePanelUtil {
        this.mMoreBottom = mMoreBottom
        this.mMoreBottom?.setOnClickListener { v: View? ->
            isNotDoubleClick().yes {
                isScroll().yes {
                    mMoreBottom?.postDelayed({
                        moreBtnClick()
                    }, 200)
                }.otherwise {
                    moreBtnClick()
                }
            }
        }
        return this
    }

    /**更多按钮点击*/
    private fun moreBtnClick() {
        mCurrentClickState = CLICK_HINT_SOFT_KEY_BOARD
        mInputEditText?.clearFocus()
        onClick(LOAD_SHOW_MORE_INPUT_PANEL)
        onDefault()
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
                        if (mCurrentShowMoreInputPanel != mSwitchType) {//加載更多操作面板
                            switchMorePanel(mSwitchType)
                        } else {
                            mInputEditText?.postDelayed({ showSoftInput() }, 50)
                        }

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
        mInputEditText?.requestFocus()
        mInputEditText?.post { mInputManager!!.showSoftInput(mInputEditText, 0) }
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
        isNotDoubleClick().yes {
            if (mMorePanelState == MORE_PANEL_STATE_SHOW) {
                hideSoftInput()
                mVoiceBottom?.postDelayed({
                    hintMorePanel()
                }, 50)
            }
        }
    }

    /**显示更多面板 供外部调用的方法*/
    fun manualShowMorePanel(isShowSoft: Boolean) {
        isNotDoubleClick().yes {
            if (mMorePanelState == MORE_PANEL_STATE_HIND) {
                morePanelGONE()
                showMorePanel()
                if (isShowSoft) {
                    mVoiceBottom?.postDelayed({
                        showSoftInput()
                    }, 50)
                }
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
                switchFragment(mMorePanelFragment, mFragmentTransaction)
            }
            LOAD_SHOW_MOTION_INPUT_PANEL -> {
                mCurrentShowMoreInputPanel = LOAD_SHOW_MOTION_INPUT_PANEL
                switchFragment(mMotionPanelFragment, mFragmentTransaction)
            }
        }
    }

    /**切换界面*/
    private fun switchFragment(mFragment: Fragment?, mFragmentTransaction: FragmentTransaction?) {
        mFragment?.let {
            mMoreContentView?.id?.let { it1 ->
                mFragmentTransaction?.replace(
                    it1, it
                )
            }
        }
        mFragmentTransaction?.commitAllowingStateLoss()
    }

    /**语音按钮状态变更*/
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

    /**表情按钮状态变更*/
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

    /**设置默认状态*/
    private fun onDefault() {
        mCurrentMotionClickState = MOTION_INPUT
        mCurrentVoiceClickState = VOICE_INPUT
        switchVoiceIcon()
        switchMotionIcon()
        mInputEditText?.clearFocus()
    }

    /**是否在短时间内双击了*/
    private fun isNotDoubleClick(): Boolean {
        (System.currentTimeMillis() - mOldTimeLong < 500).yes {
            return false
        }
        mOldTimeLong = System.currentTimeMillis()
        return true
    }

    /**列表是否有滑动到某个距离,如果是有滑动则要先滚到底部，再弹出软键盘或者操作更多的或表情的面板*/
    private fun isScroll(): Boolean {
        this.mListView?.apply {
            mListView?.adapter?.apply {
                (lastVisiblePosition != mListView?.adapter?.count?.minus(1)!!).yes {
                    mListView?.setSelection(mListView?.adapter?.count?.minus(1)!!)
                }
            }
        }
        return false
    }
}
 
 
 由于刚开始学习kotlin,所以本demo是用kotlin写的，有不好的地方望各位大神指点一二，
第一次在github上写记录分享，写的不好不要见怪哈



效果

![](http://mail.qq.com/cgi-bin/ftnExs_download?k=9bca443053fdd148a9b91e30313966650036f03033396665411a06050a08525352071d060a5b0748500301551e0153065c180152035c535c52025208550b45653756425556571400075a42545a57013a56050200030c56533b040200040b544b09450424f9859b1c699b4d78e4db2b2aa1c5c4b046f14af5&t=exs_ftn_download&code=d50039fe)
http://mail.qq.com/cgi-bin/ftnExs_download?k=9bca443053fdd148a9b91e30313966650036f03033396665411a06050a08525352071d060a5b0748500301551e0153065c180152035c535c52025208550b45653756425556571400075a42545a57013a56050200030c56533b040200040b544b09450424f9859b1c699b4d78e4db2b2aa1c5c4b046f14af5&t=exs_ftn_download&code=d50039fe

 
