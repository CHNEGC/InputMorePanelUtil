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
class MoreInputFragment : Fragment() {
    companion object {
        fun newInstance(): MoreInputFragment? {
            val args = Bundle()
            val fragment: MoreInputFragment = MoreInputFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_more_input, null)
    }
}