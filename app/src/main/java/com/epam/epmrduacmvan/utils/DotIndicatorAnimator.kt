package com.epam.epmrduacmvan.utils

import android.graphics.drawable.AnimationDrawable
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.view.get
import com.epam.epmrduacmvan.R

class DotIndicatorAnimator(val viewGroup: ViewGroup) {
    private var indicatorAnimation: Animation
    private lateinit var viewsArray: Array<View>
    private lateinit var indicatorsArray: Array<AnimationDrawable>


    init {
        initChildView()
        setupIndicators()
        indicatorAnimation = AnimationUtils.loadAnimation(viewGroup.context, R.anim.increase_indicator)

    }

    private fun initChildView() {
        viewsArray = arrayOf()
        for (i in 0 until viewGroup.childCount) {
            viewsArray += viewGroup[i]
        }
    }

    private fun setupIndicators() {
        indicatorsArray = arrayOf()
        for (i in viewsArray.indices) {
            indicatorsArray += viewsArray[i].background as AnimationDrawable
            indicatorsArray[i].isOneShot = true
        }
    }

    fun clearIndicators() {
        for (i in viewsArray.indices) {
            indicatorsArray[i].stop()
            indicatorsArray[i].selectDrawable(0)
        }
    }

    fun clearIndicator(index: Int) {
        indicatorsArray[index].stop()
        indicatorsArray[index].selectDrawable(0)
    }

    fun activateIndicator(index: Int) {
        indicatorsArray[index].start()
        viewsArray[index].startAnimation(indicatorAnimation)
    }
}