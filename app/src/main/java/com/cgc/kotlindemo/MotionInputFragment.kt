package com.cgc.kotlindemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * author: CHENGC
 * date:2020/5/5 13:43
 * describe:
 */
class MotionInputFragment : Fragment() {
    companion object {
        fun newInstance(): MotionInputFragment {
            val args = Bundle();
            val motionInputFragment: MotionInputFragment = MotionInputFragment()
            motionInputFragment.arguments = args
            return motionInputFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_motion_input, null)
    }
}