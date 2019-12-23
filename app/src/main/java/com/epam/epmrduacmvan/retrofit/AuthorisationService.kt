package com.epam.epmrduacmvan.retrofit

import com.epam.epmrduacmvan.UrlConstants.Companion.CONFIRM_EMAIL_CONTROLLER
import com.epam.epmrduacmvan.UrlConstants.Companion.SEND_CODE_CONTROLLER
import com.epam.epmrduacmvan.model.Token
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface AuthorisationService {
    @Headers( "Content-Type: application/json; charset=utf-8")

    @POST(SEND_CODE_CONTROLLER)
    fun sendCodeOnEmail(@Body email: JsonObject): Call<Void>

    @POST(CONFIRM_EMAIL_CONTROLLER)
    fun confirmEmail(@Body emailAndCode: JsonObject): Call<Token>
}