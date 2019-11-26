package com.epam.epmrduacmvan.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.epam.epmrduacmvan.R
import com.epam.epmrduacmvan.utils.DotIndicatorAnimator
import kotlinx.android.synthetic.main.fragment_authorisation_pass_code.view.*

open class AuthorisationPasscodeBaseFragment: Fragment(), View.OnClickListener, Animation.AnimationListener  {
    lateinit var indicatorsContainer: LinearLayout
    lateinit var dotIndicatorAnimator: DotIndicatorAnimator
    lateinit var shakeAnimation: Animation


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val root: View = inflater.inflate(R.layout.fragment_authorisation_pass_code, container, false)

        indicatorsContainer = root.findViewById(R.id.indicators_container)
        dotIndicatorAnimator = DotIndicatorAnimator(indicatorsContainer)
        shakeAnimation = AnimationUtils.loadAnimation(context, R.anim.shake)
        shakeAnimation.setAnimationListener(this)

        //set OnClickListener for buttons
        root.button_0.setOnClickListener(this)
        root.button_1.setOnClickListener(this)
        root.button_2.setOnClickListener(this)
        root.button_3.setOnClickListener(this)
        root.button_4.setOnClickListener(this)
        root.button_5.setOnClickListener(this)
        root.button_6.setOnClickListener(this)
        root.button_7.setOnClickListener(this)
        root.button_8.setOnClickListener(this)
        root.button_9.setOnClickListener(this)
        root.button_forgot.setOnClickListener(this)
        root.button_del.setOnClickListener(this)

        return root
    }

    override fun onClick(view: View) { /*stub*/ }

    override fun onAnimationStart(p0: Animation?) { /*stub*/ }

    override fun onAnimationRepeat(p0: Animation?) { /*stub*/ }

    override fun onAnimationEnd(p0: Animation?) {
        dotIndicatorAnimator.clearIndicators()
    }
}