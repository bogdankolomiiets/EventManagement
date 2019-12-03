package com.epam.epmrduacmvan.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.ImageButton
import com.epam.epmrduacmvan.Constants.Companion.USER_NEW_PASSCODE
import com.epam.epmrduacmvan.R
import com.epam.epmrduacmvan.utils.showCustomSnack
import kotlinx.android.synthetic.main.fragment_authorisation_pass_code.view.*

class AuthorisationReEnterPassCodeFragment : AuthorisationPasscodeBaseFragment(), View.OnClickListener {
    private lateinit var userReEnterPassCode: String
    private lateinit var userEnterPassCode: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val root = super.onCreateView(inflater, container, savedInstanceState)
        userReEnterPassCode = ""
        userEnterPassCode = arguments?.getString(USER_NEW_PASSCODE) ?: ""
        root.passcode_title_text.setText(R.string.re_enter_passcode)
        root.passcode_subtitle_text.setText(R.string.please_remember_your_passcode)
        root.button_forgot.visibility = View.INVISIBLE
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.rootView?.findViewById<ImageButton>(R.id.custom_authorisation_toolbar_up_button)?.visibility = View.VISIBLE
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.button_del -> if (userReEnterPassCode.isNotEmpty()) {
                dotIndicatorAnimator.clearIndicator(userReEnterPassCode.lastIndex)
                userReEnterPassCode = userReEnterPassCode.substring(0, userReEnterPassCode.lastIndex)
            }
            else -> {
                appendToUserReEnterPassCode(view.tag.toString())
                when (userReEnterPassCode.length) {
                    1 -> { dotIndicatorAnimator.activateIndicator(0) }
                    2 -> { dotIndicatorAnimator.activateIndicator(1) }
                    3 -> { dotIndicatorAnimator.activateIndicator(2) }
                    4 -> {
                        dotIndicatorAnimator.activateIndicator(3)
                        if (userEnterPassCode == userReEnterPassCode) {
                            savePasscode()
                        } else {
                            showCustomSnack(view, R.string.those_passwords_didnt_match_try_again)
                            userReEnterPassCode = ""
                            indicatorsContainer.startAnimation(shakeAnimation)
                        }
                    }
                }
            }
        }
    }

    override fun onAnimationEnd(p0: Animation?) {
        super.onAnimationEnd(p0)
        userReEnterPassCode = ""
    }

    private fun appendToUserReEnterPassCode(value: String) {
        userReEnterPassCode = userReEnterPassCode.plus(value)
    }

    private fun savePasscode() {
        StartActivity.obtainViewModel(this).setPassCode(userReEnterPassCode)
    }
}
