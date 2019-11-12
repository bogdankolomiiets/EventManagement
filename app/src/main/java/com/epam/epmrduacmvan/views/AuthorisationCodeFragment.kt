package com.epam.epmrduacmvan.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.epam.epmrduacmvan.Constants
import com.epam.epmrduacmvan.R
import com.epam.epmrduacmvan.utils.showSnackBar
import kotlinx.android.synthetic.main.fragment_authorisation_code.*

class AuthorisationCodeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_authorisation_code, container, false)
    }

    override fun onStart() {
        super.onStart()
        text_send_again.setOnClickListener { view ->
                showSnackBar(view, R.string.code_has_been_sent)
                println(arguments?.getInt(Constants.VERIFICATION_CODE))
        }
    }

}