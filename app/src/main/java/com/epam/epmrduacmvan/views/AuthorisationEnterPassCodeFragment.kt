package com.epam.epmrduacmvan.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.Navigation
import com.epam.epmrduacmvan.Constants.Companion.EMPTY_PASSCODE
import com.epam.epmrduacmvan.Constants.Companion.PASS_CODE
import com.epam.epmrduacmvan.AppApplication
import com.epam.epmrduacmvan.R
import kotlinx.android.synthetic.main.fragment_authorisation_pass_code.view.*
import org.mindrot.jbcrypt.BCrypt

class AuthorisationEnterPassCodeFragment : AuthorisationPasscodeBaseFragment(), View.OnClickListener{
    private var userPassCode = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val root = super.onCreateView(inflater, container, savedInstanceState)
        root.passcode_title_text.setText(R.string.enter_passcode)
        root.passcode_subtitle_text.setText(R.string.please_enter_your_passcode)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //setup toolbar
        view?.rootView?.findViewById<TextView>(R.id.custom_authorisation_toolbar_title)?.setText(R.string.passcode)
        view?.rootView?.findViewById<ImageButton>(R.id.custom_authorisation_toolbar_up_button)?.visibility = View.INVISIBLE
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.button_forgot -> Navigation.findNavController(view).navigate(R.id.action_authorisationEnterPassCodeFragment_to_authorisationEmailFragment)
            R.id.button_del -> if (userPassCode.isNotEmpty()) {
                dotIndicatorAnimator.clearIndicator(userPassCode.lastIndex)
                userPassCode = userPassCode.substring(0, userPassCode.lastIndex)
            }
            else -> {
                appendToUserPassCode(view.tag.toString())
                when (userPassCode.length) {
                    1 -> dotIndicatorAnimator.activateIndicator(0)
                    2 -> dotIndicatorAnimator.activateIndicator(1)
                    3 -> dotIndicatorAnimator.activateIndicator(2)
                    4 -> {
                        dotIndicatorAnimator.activateIndicator(3)
                        if (validatePassCode()) {
                            Navigation.findNavController(view).navigate(R.id.action_authorisationEnterPassCodeFragment_to_mainActivity)
                            activity?.finish()
                        } else {
                            indicatorsContainer.startAnimation(shakeAnimation)
                        }
                    }
                }
            }
        }
    }

    override fun onAnimationEnd(p0: Animation?) {
        super.onAnimationEnd(p0)
        userPassCode = ""
    }

    private fun validatePassCode(): Boolean {
        val tempPasscode = AppApplication.sharedPreferences.getString(PASS_CODE, "") ?: ""
        return if (tempPasscode == EMPTY_PASSCODE) {
            true
        } else {
            BCrypt.checkpw(userPassCode, tempPasscode)
        }
    }

    private fun appendToUserPassCode(value: String) {
        userPassCode = userPassCode.plus(value)
    }
}