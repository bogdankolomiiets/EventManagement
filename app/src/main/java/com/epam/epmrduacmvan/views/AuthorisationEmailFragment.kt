package com.epam.epmrduacmvan.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.epam.epmrduacmvan.Constants
import com.epam.epmrduacmvan.R
import com.epam.epmrduacmvan.utils.isOnline
import com.epam.epmrduacmvan.utils.showKeyboard
import kotlinx.android.synthetic.main.fragment_authorisation_email.*
import kotlinx.android.synthetic.main.fragment_authorisation_email.view.*

class AuthorisationEmailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_authorisation_email, container, false)

        root.email_et.requestFocus()
        showKeyboard(root.email_et)

        root.next_btn.setOnClickListener {
            if (isOnline(it.context)) {
                StartActivity.obtainViewModel(this).userEmail = email_et.text.toString()
                StartActivity.obtainViewModel(this).sendCodeOnEmail()
                it.findNavController().navigate(R.id.action_authorisationEmailFragment_to_authorisationCodeFragment)
            }
        }

        root.next_btn.isEnabled = isEmailValid(root)

        root.email_et.doAfterTextChanged { root.next_btn.isEnabled = isEmailValid(root) }

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.rootView?.findViewById<TextView>(R.id.custom_authorisation_toolbar_title)?.setText(R.string.authorisation)
        view?.rootView?.findViewById<ImageButton>(R.id.custom_authorisation_toolbar_up_button)?.visibility = View.INVISIBLE
    }

    private fun isEmailValid(root: View) = root.email_et.text.toString().matches(Constants.EMAIL_PATTERN_V2)
}