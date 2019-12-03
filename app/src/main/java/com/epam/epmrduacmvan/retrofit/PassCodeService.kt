package com.epam.epmrduacmvan.retrofit

import com.epam.epmrduacmvan.UrlConstants.Companion.PASSCODE_CONTROLLER
import com.epam.epmrduacmvan.model.Passcode
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface PassCodeService {

    @GET(PASSCODE_CONTROLLER)
    fun getPassCode(): Call<Passcode>

    @POST(PASSCODE_CONTROLLER)
    fun setPassCode(code: Passcode): Call<Int>

    @DELETE(PASSCODE_CONTROLLER)
    fun removePassCode(code: Passcode): Call<Int>
}