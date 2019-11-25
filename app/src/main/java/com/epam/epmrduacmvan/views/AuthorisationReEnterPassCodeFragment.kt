package com.epam.epmrduacmvan.views

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.epam.epmrduacmvan.Constants
import com.epam.epmrduacmvan.Constants.Companion.USER_NEW_PASSCODE
import com.epam.epmrduacmvan.R
import com.epam.epmrduacmvan.utils.DotIndicatorAnimator
import com.epam.epmrduacmvan.utils.showCustomSnack
import com.epam.epmrduacmvan.viewmodels.AuthorisationViewModel
import kotlinx.android.synthetic.main.fragment_authorisation_pass_code.*
import kotlinx.android.synthetic.main.fragment_authorisation_pass_code.view.*
import org.mindrot.jbcrypt.BCrypt

class AuthorisationReEnterPassCodeFragment : Fragment(), View.OnClickListener,
    Animation.AnimationListener {

    private lateinit var userReEnterPassCode: String
    private lateinit var userEnterPassCode: String
    private lateinit var shakeAnimation: Animation
    private lateinit var dotIndicatorAnimator: DotIndicatorAnimator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        userReEnterPassCode = ""
        shakeAnimation = AnimationUtils.loadAnimation(context, R.anim.shake)
        userEnterPassCode = arguments?.getString(USER_NEW_PASSCODE) ?: ""
        val root = inflater.inflate(R.layout.fragment_authorisation_pass_code, container, false)
        setupScreen(root)
        setOnClickListenerForButtons(root)
        dotIndicatorAnimator = DotIndicatorAnimator(root.indicators_container)
        return root
    }

    private fun setupScreen(root: View) {
        activity?.findViewById<ImageButton>(R.id.custom_authorisation_tool_bar_up_button)?.visibility =
            View.VISIBLE
        root.passcode_title_text.setText(R.string.re_enter_passcode)
        root.passcode_subtitle_text.setText(R.string.please_remember_your_passcode)
        root.button_forgot.visibility = View.INVISIBLE
    }

    private fun setOnClickListenerForButtons(root: View) {
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
    }

    override fun onClick(view: View) {
        when (view.id) {
            button_del.id -> if (userReEnterPassCode.isNotEmpty()) {
                dotIndicatorAnimator.clearIndicator(userReEnterPassCode.lastIndex)
                userReEnterPassCode =
                    userReEnterPassCode.substring(0, userReEnterPassCode.lastIndex)
            }
            else -> {
                appendToUserReEnterPassCode(view.tag.toString())
                when {
                    userReEnterPassCode.length == 1 -> {
                        dotIndicatorAnimator.activateIndicator(0)
                    }
                    userReEnterPassCode.length == 2 -> {
                        dotIndicatorAnimator.activateIndicator(1)
                    }
                    userReEnterPassCode.length == 3 -> {
                        dotIndicatorAnimator.activateIndicator(2)
                    }
                    userReEnterPassCode.length == 4 -> {
                        dotIndicatorAnimator.activateIndicator(3)
                        if (userEnterPassCode.equals(userReEnterPassCode)) {
                            putToSharedPrefUserData()
                            view.findNavController()
                                .navigate(R.id.action_authorisationReEnterPassCodeFragment_to_mainActivity)
                            activity?.finish()
                        } else {
                            showCustomSnack(view, R.string.those_passwords_didnt_match_try_again)
                            userReEnterPassCode = ""
                            indicators_container.startAnimation(shakeAnimation)
                        }
                    }
                }
            }
        }
    }

    private fun appendToUserReEnterPassCode(value: String) {
        userReEnterPassCode = userReEnterPassCode.plus(value)
    }

    private fun putToSharedPrefUserData() {
        (activity as Activity).getSharedPreferences(Constants.SHARED_PREF, Context.MODE_PRIVATE)
            .edit().putString(Constants.EMAIL,
                ViewModelProviders.of(activity as AppCompatActivity).get(AuthorisationViewModel::class.java).userEmail)
            .putString(Constants.PASS_CODE, BCrypt.hashpw(userEnterPassCode, BCrypt.gensalt()))
            .apply()
    }

    override fun onAnimationRepeat(p0: Animation?) {}

    override fun onAnimationEnd(p0: Animation?) {
        dotIndicatorAnimator.clearIndicators()
    }

    override fun onAnimationStart(p0: Animation?) {}
}
