package com.epam.epmrduacmvan.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.epam.epmrduacmvan.R
import kotlinx.android.synthetic.main.fragment_no_internet_connection.*

class NoConnectionFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_no_internet_connection, container, false)
    }

    override fun onStart() {
        super.onStart()
        val rotateAnimation = AnimationUtils.loadAnimation(context, R.anim.retry_animation)
        retry_btn.setOnClickListener { it.startAnimation(rotateAnimation) }
    }
}