package com.epam.epmrduacmvan.views

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.epam.epmrduacmvan.Constants.Companion.CODE_LENGTH
import com.epam.epmrduacmvan.R
import com.epam.epmrduacmvan.repository.BackendUserAuthorisationServiceImpl
import com.epam.epmrduacmvan.utils.showCustomSnack
import com.epam.epmrduacmvan.viewmodels.AuthorisationViewModel
import kotlinx.android.synthetic.main.fragment_authorisation_code.view.*

class AuthorisationCodeFragment : Fragment() {

    private lateinit var confirmationCode: String
    private lateinit var editTextArray: Array<EditText>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        confirmationCode = ""
        val root = inflater.inflate(R.layout.fragment_authorisation_code, container, false)

        editTextArray = arrayOf()
        for (i in 0 until root.code_container.childCount) {
            var count: Int = i
            editTextArray += root.code_container[i] as EditText
            editTextArray[i].transformationMethod = null
            editTextArray[i].doAfterTextChanged {
                if (confirmationCode.length < 3) {
                    confirmationCode += editTextArray[i].text
                    editTextArray[confirmationCode.length].requestFocus()
                } else if (confirmationCode.length == 3) {
                    confirmationCode += editTextArray[i].text

                }
                if (confirmationCode.length == CODE_LENGTH) {
//                    StartActivity.obtainViewModel(this).confirmEmail(confirmationCode)
                    view?.findNavController()
                        ?.navigate(R.id.action_authorisationCodeFragment_to_authorisationEnterNewPassCodeFragment)
                }
            }
            editTextArray[i].setOnKeyListener { _, i, _ ->
                if (i == KeyEvent.KEYCODE_DEL) {
                    removeConfirmationCode(count)
                }
                false
            }
        }

        root.text_send_again.setOnClickListener {
            BackendUserAuthorisationServiceImpl.sendCodeOnEmail(ViewModelProviders.of(activity as AppCompatActivity).get(
                AuthorisationViewModel::class.java).userEmail)
            showCustomSnack(it, R.string.code_has_been_resent)
        }

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        view?.rootView?.findViewById<TextView>(R.id.custom_authorisation_tool_bar_title)
            ?.setText(R.string.authorisation)
        view?.rootView?.findViewById<ImageButton>(R.id.custom_authorisation_tool_bar_up_button)
            ?.visibility = View.VISIBLE
    }

    private fun removeConfirmationCode(count: Int) {
        if ((confirmationCode.length >= 0) and (count > 0)) {
            editTextArray[count - 1].setText("")
            confirmationCode = confirmationCode.substring(0, count - 1)
        }
    }
}