package com.epam.epmrduacmvan.repository

import androidx.lifecycle.MutableLiveData
import com.epam.epmrduacmvan.RequestResponseCodes.Companion.BAD_REQUEST_400
import com.epam.epmrduacmvan.RequestResponseCodes.Companion.CODE_SENT_OK
import com.epam.epmrduacmvan.RequestResponseCodes.Companion.EMAIL_VERIFIED
import com.epam.epmrduacmvan.RequestResponseCodes.Companion.OK_200
import com.epam.epmrduacmvan.RequestResponseCodes.Companion.RESPONSE_BODY_TO_JSON_FAIL
import com.epam.epmrduacmvan.UrlConstants.Companion.CONFIRM_EMAIL_CONTROLLER
import com.epam.epmrduacmvan.UrlConstants.Companion.SEND_CODE_CONTROLLER
import com.epam.epmrduacmvan.UrlConstants.Companion.BASE_URL
import com.epam.epmrduacmvan.utils.showErrorToast
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

object BackendAuthorisationRepositoryImpl {
    private var okHttpClient: OkHttpClient = OkHttpClient()
    private lateinit var request: Request
    private lateinit var jsonObject: JSONObject
    private lateinit var requestBody: RequestBody
    private val mediaType = "application/json; charset=utf-8".toMediaType()

    fun sendCodeOnEmail(userEmail: String, codeRequestStatus: MutableLiveData<Int>) {
        jsonObject = JSONObject()
        jsonObject.put("email", userEmail)
        requestBody = jsonObject.toString().toRequestBody(mediaType)

        request = Request.Builder()
            .url(BASE_URL.plus(SEND_CODE_CONTROLLER))
            .method("POST", requestBody)
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                call.cancel()
                showErrorToast("Failure: " + e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.code == OK_200) {
                    codeRequestStatus.postValue(CODE_SENT_OK)
                } else {
                    codeRequestStatus.postValue(BAD_REQUEST_400)
                }
            }
        })
    }

    fun confirmEmail(userEmail: String, codeFromEmail: String, codeRequestStatus: MutableLiveData<Int>, tokenFromRequest: MutableLiveData<String>) {
        jsonObject = JSONObject()
        jsonObject.put("email", userEmail).put("code", codeFromEmail)
        requestBody = jsonObject.toString().toRequestBody(mediaType)

        request = Request.Builder()
                .url(BASE_URL.plus(CONFIRM_EMAIL_CONTROLLER))
                .method("POST", requestBody)
                .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                call.cancel()
                showErrorToast("Failure: " + e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.code == OK_200) {
                    try {
                        val jsonData = JSONObject(response.body?.string())
                        tokenFromRequest.postValue(jsonData.getString("token"))
                        codeRequestStatus.postValue(EMAIL_VERIFIED)
                    } catch (ex: Exception) {
                        codeRequestStatus.postValue(RESPONSE_BODY_TO_JSON_FAIL)
                    }
                } else {
                    codeRequestStatus.postValue(BAD_REQUEST_400)
                }
            }
        })
    }
}
