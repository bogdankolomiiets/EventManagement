package com.epam.epmrduacmvan.retrofit

import com.epam.epmrduacmvan.UrlConstants.Companion.PASSCODE_CONTROLLER
import com.epam.epmrduacmvan.model.Passcode
import retrofit2.Call
import retrofit2.http.*

interface PassCodeService {
    @Headers( "Content-Type: application/json; charset=utf-8")

    /*reserved
    *@GET(PASSCODE_CONTROLLER)
    *fun getPassCode(): Call<Passcode>
    */

    @POST(PASSCODE_CONTROLLER)
    fun setPassCode(@Body code: Passcode): Call<Void>

    /*reserved
    *@DELETE(PASSCODE_CONTROLLER)
    *fun removePassCode(@Body code: Passcode): Call<Void>
    */
}