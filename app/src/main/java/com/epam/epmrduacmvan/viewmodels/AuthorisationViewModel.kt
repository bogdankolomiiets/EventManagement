package com.epam.epmrduacmvan.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.epam.epmrduacmvan.AppApplication
import com.epam.epmrduacmvan.Constants.Companion.EMAIL
import com.epam.epmrduacmvan.Constants.Companion.EMPTY_PASSCODE
import com.epam.epmrduacmvan.Constants.Companion.PASS_CODE
import com.epam.epmrduacmvan.Constants.Companion.TOKEN_SIGN
import com.epam.epmrduacmvan.Constants.Companion.USER_TOKEN
import com.epam.epmrduacmvan.RequestResponseCodes.Companion.CODE_SENT_OK
import com.epam.epmrduacmvan.RequestResponseCodes.Companion.EMAIL_VERIFIED
import com.epam.epmrduacmvan.RequestResponseCodes.Companion.INTERNAL_SERVER_ERROR_500
import com.epam.epmrduacmvan.RequestResponseCodes.Companion.PASSCODE_SET_NOT_EMPTY
import com.epam.epmrduacmvan.model.Passcode
import com.epam.epmrduacmvan.model.Token
import com.epam.epmrduacmvan.retrofit.AuthorisationService
import com.epam.epmrduacmvan.retrofit.PassCodeService
import com.epam.epmrduacmvan.retrofit.RetrofitInstance
import com.epam.epmrduacmvan.utils.isOnline
import com.epam.epmrduacmvan.utils.showErrorToast
import com.google.gson.JsonObject
import org.mindrot.jbcrypt.BCrypt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class AuthorisationViewModel : ViewModel() {
    private val context: Context = AppApplication.appContext
    private var tempPassCode = EMPTY_PASSCODE
    private val retrofit: Retrofit = RetrofitInstance.retrofit
    private val authorisationService: AuthorisationService
    private val passCodeService: PassCodeService
    val codeRequestStatus = MutableLiveData<Int>()
    val passCodeRequestStatus = MutableLiveData<Int>()
    val passCodeFromRequest = MutableLiveData<String>()
    var tokenFromRequest = MutableLiveData<Token>()
    var userEmail: String = ""

    init {
        authorisationService = retrofit.create(AuthorisationService::class.java)
        passCodeService = retrofit.create(PassCodeService::class.java)

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
            val jsonObject = JsonObject()
            jsonObject.addProperty("email", userEmail)

            authorisationService.sendCodeOnEmail(jsonObject).enqueue(object : Callback<Void> {
                override fun onFailure(call: Call<Void>?, t: Throwable) {
                    showErrorToast("Failure: ${t.message}")
                }

                override fun onResponse(call: Call<Void>?, response: Response<Void>) {
                    if (!response.isSuccessful) {
                        showErrorToast("Failure: ${response.code()}")
                        return
                    }
                    codeRequestStatus.postValue(CODE_SENT_OK)
                }
            })
        }
    }

    fun confirmEmail(code: String) {
        if (isOnline(context)) {
            val jsonObject = JsonObject()
            jsonObject.addProperty("email", userEmail)
            jsonObject.addProperty("code", code)

            authorisationService.confirmEmail(jsonObject).enqueue(object : Callback<Token> {
                override fun onFailure(call: Call<Token>?, t: Throwable) {
                    codeRequestStatus.postValue(INTERNAL_SERVER_ERROR_500)
                }

                override fun onResponse(call: Call<Token>?, response: Response<Token>) {
                    if (!response.isSuccessful) {
                        codeRequestStatus.postValue(response.code())
                        return
                    }
                    tokenFromRequest.postValue(response.body())
                    codeRequestStatus.postValue(EMAIL_VERIFIED)
                }
            })
        }
    }

    fun getPassCode(passCode: String) {
        if (isOnline(context)) {
            //todo
            passCodeService.getPassCode()
        }
    }

    fun setPassCode(code: String) {
        if (isOnline(context)) {
            tempPassCode = code
            passCodeService.setPassCode(Passcode(code)).enqueue(object : Callback<Int>{
                override fun onFailure(call: Call<Int>?, t: Throwable) {
                    showErrorToast("Failure: ${t.message}")
                }

                override fun onResponse(call: Call<Int>?, response: Response<Int>) {
                    if (!response.isSuccessful) {
                        showErrorToast("Failure: ${response.code()}")
                        passCodeRequestStatus.postValue(response.code())
                        return
                    }
                    passCodeRequestStatus.postValue(PASSCODE_SET_NOT_EMPTY)
                }
            })
        }
    }

    fun removePassCode(code: String) {
        if (isOnline(context)) {
            //todo
            passCodeService.removePassCode(Passcode(code))
        }
    }
}