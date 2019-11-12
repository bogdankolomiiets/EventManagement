package com.epam.epmrduacmvan.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.epam.epmrduacmvan.Constants
import com.epam.epmrduacmvan.R
import com.epam.epmrduacmvan.repository.BackendUserAuthorisationServiceImpl
import kotlinx.android.synthetic.main.fragment_authorisation_email.*

class AuthorisationEmailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_authorisation_email, container, false)
    }

    override fun onStart() {
        super.onStart()
        //if user pressed button "back" from previous screen we need to validate email and set nextBtn.isEnabled
        next_btn.isEnabled = isEmailValid()

        next_btn.setOnClickListener {
            sendVerificationCodeToEmail(email_et.text.toString())
            it.findNavController().navigate(R.id.action_authorisationEmailFragment_to_authorisationCodeFragment)
        }

        email_et.doAfterTextChanged {
            next_btn.isEnabled = isEmailValid()
        }
    }

    private fun sendVerificationCodeToEmail(email: String) {
        BackendUserAuthorisationServiceImpl.sendCodeOnEmail(email)
    }

    private fun isEmailValid() = email_et.text.toString().matches(Constants.EMAIL_PATTERN_V2)
}