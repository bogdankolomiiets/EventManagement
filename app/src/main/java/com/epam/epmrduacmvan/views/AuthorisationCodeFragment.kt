package com.epam.epmrduacmvan.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.get
import androidx.core.widget.doAfterTextChanged
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.epam.epmrduacmvan.Constants.Companion.CODE_LENGTH
import androidx.lifecycle.Observer
import com.epam.epmrduacmvan.R
import com.epam.epmrduacmvan.RequestResponseCodes.Companion.BAD_REQUEST_400
import com.epam.epmrduacmvan.RequestResponseCodes.Companion.CODE_SENT_OK
import com.epam.epmrduacmvan.RequestResponseCodes.Companion.EMAIL_VERIFIED
import com.epam.epmrduacmvan.RequestResponseCodes.Companion.ERASE_CODE
import com.epam.epmrduacmvan.RequestResponseCodes.Companion.INTERNAL_SERVER_ERROR_500
import com.epam.epmrduacmvan.RequestResponseCodes.Companion.NOT_FOUND_404
import com.epam.epmrduacmvan.RequestResponseCodes.Companion.RESPONSE_BODY_TO_JSON_FAIL
import com.epam.epmrduacmvan.utils.showCustomSnack
import kotlinx.android.synthetic.main.fragment_authorisation_code.view.*
import com.epam.epmrduacmvan.utils.isOnline

class AuthorisationCodeFragment : Fragment() {
    private lateinit var codeContainer: LinearLayout
    private lateinit var confirmationCode: String
    private lateinit var editTextArray: Array<EditText>
    private lateinit var shakeAnimation: Animation

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        confirmationCode = ""
        val root = inflater.inflate(R.layout.fragment_authorisation_code, container, false)
        codeContainer = root.findViewById(R.id.code_container)

        shakeAnimation = AnimationUtils.loadAnimation(context, R.anim.shake)
        shakeAnimation.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationRepeat(p0: Animation?) {/* stub */}
            override fun onAnimationStart(p0: Animation?) {/* stub */}
            override fun onAnimationEnd(p0: Animation?) { clear() }
        })

        editTextArray = arrayOf()
        for (i in 0 until codeContainer.childCount) {
            val count: Int = i

            editTextArray += codeContainer[i] as EditText
            editTextArray[i].transformationMethod = null
            editTextArray[i].setOnTouchListener { _: View?, _: MotionEvent? ->
                return@setOnTouchListener true
            }

            editTextArray[i].doAfterTextChanged {
                if (confirmationCode.length < 3) {
                    confirmationCode += editTextArray[i].text
                    editTextArray[confirmationCode.length].requestFocus()
                } else if (confirmationCode.length == 3) {
                    confirmationCode += editTextArray[i].text
                }
                if (confirmationCode.length == CODE_LENGTH) {
                    StartActivity.obtainViewModel(this).confirmEmail(confirmationCode)
                }
            }
            editTextArray[i].setOnKeyListener { _, eventCode, _ ->
                if (eventCode == KeyEvent.KEYCODE_DEL) {
                    removeConfirmationCode(count)
                }
                false
            }
        }

        root.text_send_again.setOnClickListener {
            if (isOnline(it.context)) {
                StartActivity.obtainViewModel(this).sendCodeOnEmail()
            }
        }

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        view?.rootView?.findViewById<TextView>(R.id.custom_authorisation_toolbar_title)?.setText(R.string.authorisation)
        view?.rootView?.findViewById<ImageButton>(R.id.custom_authorisation_toolbar_up_button)?.visibility = View.VISIBLE

        StartActivity.obtainViewModel(this).codeRequestStatus.observe(this, Observer {
            when (it) {
                EMAIL_VERIFIED -> codeContainer.findNavController().navigate(R.id.action_authorisationCodeFragment_to_authorisationEnterNewPassCodeFragment)
                CODE_SENT_OK -> showCustomSnack(codeContainer, R.string.code_has_sent)
                BAD_REQUEST_400 -> showCustomSnack(codeContainer, R.string.confirmation_error).also { codeContainer.startAnimation(shakeAnimation) }
                NOT_FOUND_404 -> showCustomSnack(codeContainer, R.string.not_found)
                INTERNAL_SERVER_ERROR_500 -> showCustomSnack(codeContainer, R.string.internal_server_error).also { clear() }
                RESPONSE_BODY_TO_JSON_FAIL -> showCustomSnack(codeContainer, R.string.bad_response).also { clear() }
            }
        })
    }

    private fun removeConfirmationCode(count: Int) {
        if ((confirmationCode.length >= 0) and (count > 0)) {
            editTextArray[count - 1].setText("")
            confirmationCode = confirmationCode.substring(0, count - 1)
        }
    }

    private fun clearCodeContainer() {
        for (i in 0 .. editTextArray.lastIndex) {
            editTextArray[i].text.clear()
        }
    }

    private fun clear(){
        confirmationCode = ""
        clearCodeContainer()
        editTextArray[0].requestFocus()
    }

    override fun onDestroy() {
        super.onDestroy()
        StartActivity.obtainViewModel(this).codeRequestStatus.postValue(ERASE_CODE)
    }
}