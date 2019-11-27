package com.epam.epmrduacmvan.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.epam.epmrduacmvan.AppApplication
import com.epam.epmrduacmvan.Constants.Companion.EMAIL
import com.epam.epmrduacmvan.Constants.Companion.EMPTY_PASSCODE
import com.epam.epmrduacmvan.Constants.Companion.PASS_CODE
import com.epam.epmrduacmvan.Constants.Companion.TOKEN_SIGN
import com.epam.epmrduacmvan.Constants.Companion.USER_TOKEN
import com.epam.epmrduacmvan.RequestResponseCodes.Companion.PASSCODE_SET_NOT_EMPTY
import com.epam.epmrduacmvan.repository.*
import com.epam.epmrduacmvan.utils.isOnline
import org.mindrot.jbcrypt.BCrypt

class AuthorisationViewModel(application: Application) : AndroidViewModel(application) {
    private val context: Context = application
    private var tempPassCode = EMPTY_PASSCODE
    private val authorisationRepository: AuthorisationRepository = RepositoryProvider.authorisationRepository
    private val passCodeRepository: PassCodeRepository = RepositoryProvider.passcodeRepository
    val codeRequestStatus = MutableLiveData<Int>()
    val passCodeRequestStatus = MutableLiveData<Int>()
    val passCodeFromRequest = MutableLiveData<String>()
    var tokenFromRequest = MutableLiveData<String>()
    var userEmail: String = ""

    init {
        passCodeFromRequest.observeForever { AppApplication.sharedPreferences
            .edit()
            .putString(PASS_CODE, it)
            .apply() }

        tokenFromRequest.observeForever { AppApplication.sharedPreferences
            .edit()
            .putString(USER_TOKEN, TOKEN_SIGN.plus(it))
            .apply()

            AppApplication.token = TOKEN_SIGN.plus(it)
        }

        passCodeRequestStatus.observeForever {
            if (it == PASSCODE_SET_NOT_EMPTY) {
                AppApplication.sharedPreferences
                        .edit()
                        .putString(EMAIL, userEmail)
                        .putString(PASS_CODE, BCrypt.hashpw(tempPassCode, BCrypt.gensalt()))
                        .apply()
            }
        }
    }

    fun sendCodeOnEmail() {
        if (isOnline(context)) {
            authorisationRepository.sendCodeOnEmail(userEmail, codeRequestStatus)
        }
    }

    fun confirmEmail(code: String) {
        if (isOnline(context)) {
            authorisationRepository.confirmEmail(userEmail, code, codeRequestStatus, tokenFromRequest)
        }
    }

    fun getPassCode(passCode: String) {
        if (isOnline(context)) {
            passCodeRepository.getPassCode(passCodeRequestStatus, passCodeFromRequest)
        }
    }

    fun setPassCode(passCode: String) {
        if (isOnline(context)) {
            tempPassCode = passCode
            passCodeRepository.setPassCode(passCode, passCodeRequestStatus)
        }
    }

    fun removePassCode(passCode: String) {
        if (isOnline(context)) {
            passCodeRepository.removePassCode(passCode, passCodeRequestStatus)
        }
    }
}