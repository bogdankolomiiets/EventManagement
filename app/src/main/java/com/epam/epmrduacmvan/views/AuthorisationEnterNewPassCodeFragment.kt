package com.epam.epmrduacmvan.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.findNavController
import com.epam.epmrduacmvan.Constants.Companion.EMAIL
import com.epam.epmrduacmvan.Constants.Companion.USER_NEW_PASSCODE
import com.epam.epmrduacmvan.Constants.Companion.EMPTY_PASSCODE
import com.epam.epmrduacmvan.Constants.Companion.PASS_CODE
import com.epam.epmrduacmvan.AppApplication
import com.epam.epmrduacmvan.R
import com.epam.epmrduacmvan.RequestResponseCodes.Companion.PASSCODE_SET_EMPTY
import com.epam.epmrduacmvan.utils.hideKeyboard
import kotlinx.android.synthetic.main.fragment_authorisation_pass_code.view.*

class AuthorisationEnterNewPassCodeFragment : AuthorisationPasscodeBaseFragment(), View.OnClickListener {
    private var userNewPassCode = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val root = super.onCreateView(inflater, container, savedInstanceState)
        hideKeyboard(root)
        root.button_forgot.setText(R.string.button_skip)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.rootView?.findViewById<TextView>(R.id.custom_authorisation_toolbar_title)?.setText(R.string.set_passcode)
        view?.rootView?.findViewById<ImageButton>(R.id.custom_authorisation_toolbar_up_button)?.visibility = View.INVISIBLE
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.button_forgot -> { savePasscode() }
            R.id.button_del -> if (userNewPassCode.isNotEmpty()) {
                dotIndicatorAnimator.clearIndicator(userNewPassCode.lastIndex)
                userNewPassCode = userNewPassCode.substring(0, userNewPassCode.lastIndex)
            }
            else -> {
                appendToUserNewPassCode(view.tag.toString())
                when (userNewPassCode.length) {
                    1 -> { dotIndicatorAnimator.activateIndicator(0) }
                    2 -> { dotIndicatorAnimator.activateIndicator(1) }
                    3 -> { dotIndicatorAnimator.activateIndicator(2) }
                    4 -> {
                        dotIndicatorAnimator.activateIndicator(3)
                        val bundle = Bundle()
                        bundle.putString(USER_NEW_PASSCODE, userNewPassCode)
                        view.findNavController().navigate(R.id.action_authorisationEnterNewPassCodeFragment_to_authorisationReEnterPassCodeFragment, bundle)
                    }
                }
            }
        }
    }

    private fun appendToUserNewPassCode(value: String) {
        userNewPassCode = userNewPassCode.plus(value)
    }

    private fun savePasscode() {
        AppApplication.sharedPreferences
            .edit()
            .putString(EMAIL, StartActivity.obtainViewModel(this).userEmail)
            .putString(PASS_CODE, EMPTY_PASSCODE)
            .apply()

        StartActivity.obtainViewModel(this).passCodeRequestStatus.postValue(PASSCODE_SET_EMPTY)

    }
}