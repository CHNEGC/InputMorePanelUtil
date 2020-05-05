package com.cgc.kotlindemo

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.cgc.kotlindemo.InputMorePanelUtil.Companion.MOTION_INPUT
import com.cgc.kotlindemo.InputMorePanelUtil.Companion.MOTION_SOFT_KEY_BOARD_INPUT
import com.cgc.kotlindemo.InputMorePanelUtil.Companion.VOICE_INPUT
import com.cgc.kotlindemo.InputMorePanelUtil.Companion.VOICE_SOFT_KEY_BOARD_INPUT


class MainActivity : AppCompatActivity() {

    private val mView: View by lazy { findViewById<View>(R.id.view) }
    private val mView2: FrameLayout by lazy { findViewById<FrameLayout>(R.id.view2) }
    private val mEditText: EditText by lazy { findViewById<EditText>(R.id.et) }
    private val mListView: ListView by lazy { findViewById<ListView>(R.id.listView) }
    private val btnSwitchVoice: Button by lazy { findViewById<Button>(R.id.btnSwitchVoice) }
    private val btnEmotion: Button by lazy { findViewById<Button>(R.id.btnEmotion) }
    private val tvLongVoice: TextView by lazy { findViewById<TextView>(R.id.tvLongVoice) }

    private var mInputMorePanelUtil: InputMorePanelUtil? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mListView.apply {
            adapter = ListAdapter(this@MainActivity)
        }

        mInputMorePanelUtil = InputMorePanelUtil.with(this, supportFragmentManager).apply {
            bindContentView(mView)
            bindInputEditText(mEditText)
            bindMorePanelView(mView2)
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
                        mEditText.visibility = View.GONE
                        btnSwitchVoice.text = "键盘"
                    }
                }
            }
            bindMoreBottom(findViewById(R.id.btnMore))
            bindMoreInputFragment(MoreInputFragment.newInstance()!!)
            bindMotionInputFragment(MotionInputFragment.newInstance()!!)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                return if (mInputMorePanelUtil?.interceptBackPress()!!) true else super.onKeyDown(
                    keyCode,
                    event
                )
            }
        }
        return super.onKeyDown(keyCode, event)
    }

//    fun onLogin(view: View) {
//        mView.apply {
//            when (isShowMorePage) {
//                false -> {
//                    openMorePage(mView)
//                }
//                true -> {
//                    closeMorePage(mView)
//                }
//            }
//        }
//    }

//    private fun openMorePage(mView: View?, startValue: Float = 0f, endValue: Float = 750f) {
//        isShowMorePage = true
//        mView?.apply {
//            val mValueAnimation: ValueAnimator = ValueAnimator.ofFloat(startValue, -endValue)
//            mValueAnimation.apply {
//                duration = 300
//                addUpdateListener {
//                    val v: Float = it.animatedValue as Float
//                    translationY = v
//                    mView2.translationY = v
//                }
//                interpolator = LinearInterpolator()
//                start()
//            }
//        }
//    }
//
//    private fun closeMorePage(mView: View?, startValue: Float = 750f, endValue: Float = 0f) {
//        isShowMorePage = false
//        mView?.apply {
//            val mValueAnimation: ValueAnimator = ValueAnimator.ofFloat(-startValue, endValue)
//            mValueAnimation.apply {
//                duration = 300
//                addUpdateListener {
//                    val v: Float = it.animatedValue as Float
//                    translationY = v
//                    mView2.translationY = v
//                }
//                interpolator = LinearInterpolator()
//                start()
//            }
//        }
//    }
//
//    fun onLoginOut(view: View) {
//        mView.apply {
//
//        }
//    }

    class ListAdapter(private val mContext: Context) : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            return View.inflate(mContext, R.layout.view_item, null)
        }

        override fun getItem(position: Int): Any {
            return position
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int = 20
    }
}
