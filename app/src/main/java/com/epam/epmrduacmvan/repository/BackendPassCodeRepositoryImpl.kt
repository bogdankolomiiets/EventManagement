package com.epam.epmrduacmvan.repository

import androidx.lifecycle.MutableLiveData
import com.epam.epmrduacmvan.AppApplication
import com.epam.epmrduacmvan.Constants.Companion.TOKEN_HEADER_NAME
import com.epam.epmrduacmvan.RequestResponseCodes.Companion.BAD_REQUEST_400
import com.epam.epmrduacmvan.RequestResponseCodes.Companion.INTERNAL_SERVER_ERROR_500
import com.epam.epmrduacmvan.RequestResponseCodes.Companion.OK_200
import com.epam.epmrduacmvan.RequestResponseCodes.Companion.PASSCODE_REMOVED
import com.epam.epmrduacmvan.RequestResponseCodes.Companion.PASSCODE_SET
import com.epam.epmrduacmvan.RequestResponseCodes.Companion.RESPONSE_BODY_TO_JSON_FAIL
import com.epam.epmrduacmvan.UrlConstants.Companion.PASSCODE_CONTROLLER
import com.epam.epmrduacmvan.UrlConstants.Companion.URL
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

object BackendPassCodeRepositoryImpl: PassCodeRepository {
    private var okHttpClient: OkHttpClient = OkHttpClient()
    private lateinit var request: Request
    private lateinit var jsonObject: JSONObject
    private lateinit var requestBody: RequestBody
    private val mediaType = "application/json; charset=utf-8".toMediaType()

    override fun getPassCode(passCodeRequestStatus: MutableLiveData<Int>, passCodeFromRequest: MutableLiveData<String>) {
        request = Request.Builder()
                .header(TOKEN_HEADER_NAME, AppApplication.token)
                .url(URL.plus(PASSCODE_CONTROLLER))
                .get()
                .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                call.cancel()
                passCodeRequestStatus.postValue(INTERNAL_SERVER_ERROR_500)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.code == OK_200) {
                    try {
                        passCodeFromRequest.postValue(JSONObject(response.body?.string()).getString("passcodeHash"))
                    } catch (ex: Exception) {
                        passCodeRequestStatus.postValue(RESPONSE_BODY_TO_JSON_FAIL)
                    }
                } else {
                    passCodeRequestStatus.postValue(BAD_REQUEST_400)
                }
            }
        })
    }

    override fun setPassCode(passCode: String, passCodeRequestStatus: MutableLiveData<Int>) {
        jsonObject = JSONObject()
        jsonObject.put("code", passCode)
        requestBody = jsonObject.toString().toRequestBody(mediaType)

        request = Request.Builder()
            .header(TOKEN_HEADER_NAME, AppApplication.token)
            .url(URL.plus(PASSCODE_CONTROLLER))
            .method("POST", requestBody)
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                call.cancel()
                passCodeRequestStatus.postValue(INTERNAL_SERVER_ERROR_500)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.code == OK_200) {
                    passCodeRequestStatus.postValue(PASSCODE_SET)
                } else {
                    passCodeRequestStatus.postValue(BAD_REQUEST_400)
                }
            }
        })
    }

    override fun removePassCode(passCode: String, passCodeRequestStatus: MutableLiveData<Int>) {
        jsonObject = JSONObject()
        jsonObject.put("code", passCode)
        requestBody = jsonObject.toString().toRequestBody(mediaType)

        request = Request.Builder()
                .header(TOKEN_HEADER_NAME, AppApplication.token)
                .url(URL.plus(PASSCODE_CONTROLLER))
                .method("DELETE", requestBody)
                .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                call.cancel()
                passCodeRequestStatus.postValue(INTERNAL_SERVER_ERROR_500)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.code == OK_200) {
                    passCodeRequestStatus.postValue(PASSCODE_REMOVED)
                } else {
                    passCodeRequestStatus.postValue(BAD_REQUEST_400)
                }
            }
        })
    }
}