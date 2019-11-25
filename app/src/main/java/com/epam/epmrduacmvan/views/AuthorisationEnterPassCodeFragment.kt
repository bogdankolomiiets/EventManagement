package com.epam.epmrduacmvan.views

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.epam.epmrduacmvan.Constants.Companion.PASS_CODE
import com.epam.epmrduacmvan.Constants.Companion.SHARED_PREF
import com.epam.epmrduacmvan.R
import com.epam.epmrduacmvan.utils.DotIndicatorAnimator
import kotlinx.android.synthetic.main.fragment_authorisation_pass_code.*
import kotlinx.android.synthetic.main.fragment_authorisation_pass_code.view.*
import org.mindrot.jbcrypt.BCrypt

class AuthorisationEnterPassCodeFragment : Fragment(), View.OnClickListener,
    Animation.AnimationListener {
    private lateinit var userPassCode: String
    private lateinit var shakeAnimation: Animation
    private lateinit var dotIndicatorAnimator: DotIndicatorAnimator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        userPassCode = ""
        shakeAnimation = AnimationUtils.loadAnimation(context, R.anim.shake)
        val root = inflater.inflate(R.layout.fragment_authorisation_pass_code, container, false)

        root.passcode_title_text.setText(R.string.enter_passcode)
        root.passcode_subtitle_text.setText(R.string.please_enter_your_passcode)

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
        shakeAnimation.setAnimationListener(this)

        dotIndicatorAnimator = DotIndicatorAnimator(root.indicators_container)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.rootView?.findViewById<TextView>(R.id.custom_authorisation_tool_bar_title)
            ?.setText(R.string.passcode)
        view?.rootView?.findViewById<ImageButton>(R.id.custom_authorisation_tool_bar_up_button)
            ?.visibility = View.INVISIBLE
    }

    override fun onClick(view: View) {
        when (view.id) {
            button_forgot.id -> Navigation.findNavController(view).navigate(R.id.action_authorisationEnterPassCodeFragment_to_authorisationEmailFragment)
            button_del.id -> if (userPassCode.isNotEmpty()) {
                dotIndicatorAnimator.clearIndicator(userPassCode.lastIndex)
                userPassCode = userPassCode.substring(0, userPassCode.lastIndex)
            }
            else -> {
                appendToUserPassCode(view.tag.toString())
                when {
                    userPassCode.length == 1 -> {
                        dotIndicatorAnimator.activateIndicator(0)
                    }
                    userPassCode.length == 2 -> {
                        dotIndicatorAnimator.activateIndicator(1)
                    }
                    userPassCode.length == 3 -> {
                        dotIndicatorAnimator.activateIndicator(2)
                    }
                    userPassCode.length == 4 -> {
                        dotIndicatorAnimator.activateIndicator(3)
                        if (validatePassCode()) {
                            Navigation.findNavController(view)
                                .navigate(R.id.action_authorisationEnterPassCodeFragment_to_mainActivity)
                            activity?.finish()
                        } else {
                            indicators_container.startAnimation(shakeAnimation)
                        }
                    }
                }
            }
        }
    }

    private fun validatePassCode(): Boolean {
        return BCrypt.checkpw(userPassCode,
            activity?.getSharedPreferences(SHARED_PREF, MODE_PRIVATE)?.getString(PASS_CODE, ""))
    }

    private fun appendToUserPassCode(value: String) {
        userPassCode = userPassCode.plus(value)
    }

    override fun onAnimationRepeat(p0: Animation?) {}

    override fun onAnimationEnd(p0: Animation?) {
        dotIndicatorAnimator.clearIndicators()
        userPassCode = ""
    }

    override fun onAnimationStart(p0: Animation?) {}
}
