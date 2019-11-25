package com.epam.epmrduacmvan.views

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.epam.epmrduacmvan.Constants
import com.epam.epmrduacmvan.Constants.Companion.EMAIL
import com.epam.epmrduacmvan.Constants.Companion.PASS_CODE_LENGTH
import com.epam.epmrduacmvan.Constants.Companion.USER_NEW_PASSCODE
import com.epam.epmrduacmvan.R
import com.epam.epmrduacmvan.utils.DotIndicatorAnimator
import com.epam.epmrduacmvan.utils.hideKeyboard
import com.epam.epmrduacmvan.viewmodels.AuthorisationViewModel
import kotlinx.android.synthetic.main.fragment_authorisation_pass_code.*
import kotlinx.android.synthetic.main.fragment_authorisation_pass_code.view.*

class AuthorisationEnterNewPassCodeFragment : Fragment(), View.OnClickListener {

    private lateinit var userNewPassCode: String
    private lateinit var dotIndicatorAnimator: DotIndicatorAnimator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        userNewPassCode = ""
        val root = inflater.inflate(R.layout.fragment_authorisation_pass_code, container, false)
        hideKeyboard(root)
        root.button_forgot.setText(R.string.button_skip)

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

        dotIndicatorAnimator = DotIndicatorAnimator(root.indicators_container)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.rootView?.findViewById<TextView>(R.id.custom_authorisation_tool_bar_title)
            ?.setText(R.string.set_passcode)
        view?.rootView?.findViewById<ImageButton>(R.id.custom_authorisation_tool_bar_up_button)
            ?.visibility = View.INVISIBLE
    }

    override fun onClick(view: View) {
        when (view.id) {
            button_forgot.id -> {
                putToSharedPrefUserData()
                Navigation.findNavController(view)
                    .navigate(R.id.action_authorisationEnterNewPassCodeFragment_to_mainActivity)
                activity?.finish()
            }
            button_del.id -> if (userNewPassCode.isNotEmpty()) {
                dotIndicatorAnimator.clearIndicator(userNewPassCode.lastIndex)
                userNewPassCode = userNewPassCode.substring(0, userNewPassCode.lastIndex)
            }
            else -> {
                appendToUserNewPassCode(view.tag.toString())
                when {
                    userNewPassCode.length == 1 -> {
                        dotIndicatorAnimator.activateIndicator(0)
                    }
                    userNewPassCode.length == 2 -> {
                        dotIndicatorAnimator.activateIndicator(1)
                    }
                    userNewPassCode.length == 3 -> {
                        dotIndicatorAnimator.activateIndicator(2)
                    }
                    userNewPassCode.length == 4 -> {
                        dotIndicatorAnimator.activateIndicator(3)
                        val bundle = Bundle()
                        bundle.putString(USER_NEW_PASSCODE, userNewPassCode)
                        view.findNavController()
                            .navigate(R.id.action_authorisationEnterNewPassCodeFragment_to_authorisationReEnterPassCodeFragment,
                                bundle)
                    }
                }
            }
        }
    }

    private fun appendToUserNewPassCode(value: String) {
        userNewPassCode = userNewPassCode.plus(value)
    }

    private fun putToSharedPrefUserData() {
        (activity as Activity).getSharedPreferences(Constants.SHARED_PREF, Context.MODE_PRIVATE)
            .edit().putString(EMAIL,
                ViewModelProviders.of(activity as AppCompatActivity).get(AuthorisationViewModel::class.java).userEmail)
            .apply()
    }
}