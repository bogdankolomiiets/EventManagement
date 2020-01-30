package com.epam.epmrduacmvan.retrofit

import com.epam.epmrduacmvan.UrlConstants.Companion.ARTIFACT_CONTROLLER
import com.epam.epmrduacmvan.UrlConstants.Companion.CATEGORY_CONTROLLER
import com.epam.epmrduacmvan.UrlConstants.Companion.CITY_CONTROLLER
import com.epam.epmrduacmvan.UrlConstants.Companion.COUNTRY_CONTROLLER
import com.epam.epmrduacmvan.UrlConstants.Companion.LANGUAGES_CONTROLLER
import com.epam.epmrduacmvan.UrlConstants.Companion.LINK_CONTROLLER
import com.epam.epmrduacmvan.UrlConstants.Companion.SPEAKERS_CONTROLLER
import com.epam.epmrduacmvan.UrlConstants.Companion.USER_PROFILE_CONTROLLER
import com.epam.epmrduacmvan.model.*
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*
import java.io.File

interface AdditionalDataService {
    @Headers( "Content-Type: application/json; charset=UTF-8")

    @GET(LANGUAGES_CONTROLLER)
    fun getLanguage(): Call<List<Language>>

    @GET(CITY_CONTROLLER)
    fun getCity(): Call<List<City>>

    /*reserved
    *@GET(CITY_CONTROLLER.plus("/{countryId}"))
    *fun getCityByCountry(@Path("countryId") countryId: Int ): Call<List<City>>
     */

    @GET(COUNTRY_CONTROLLER)
    fun getCountry(): Call<List<Country>>

    @GET(CATEGORY_CONTROLLER)
    fun getCategory(): Call<List<Category>>

    /*reserved
    *@GET(COMPANY_CONTROLLER)
    *fun getCompany(): Call<List<Company>>
     */

    @GET(SPEAKERS_CONTROLLER.plus("/{search}"))
    fun getSpeakers(@Path("search") search: String ): Call<List<Speakers>>

    @GET(USER_PROFILE_CONTROLLER)
    fun getUser(): Call<User>

    @DELETE(ARTIFACT_CONTROLLER.plus("/artifactId"))
    fun deleteArtifact(@Path("artifactId") artifactId: String)

    @GET(ARTIFACT_CONTROLLER.plus("/eventId"))
    fun getArtifacts(@Path("eventId") eventId: String): Call<List<Artifact>>

    @GET(LINK_CONTROLLER.plus("/eventId"))
    fun getAllEventLinks(@Path("eventId") eventId: String): Call<List<Artifact>>

    @POST(LINK_CONTROLLER.plus("/{eventId}"))
    fun addLinkToEvent(@Path("eventId") eventId: String, @Body artifactDto: Artifact): Call<Void>

    @Multipart
    @POST(ARTIFACT_CONTROLLER.plus("/{eventId}"))
    fun uploadArtifact(@Path("eventId") eventId: String, @Part file: MultipartBody.Part): Call<Void>

    @DELETE(LINK_CONTROLLER.plus("/linkId"))
    fun deleteLink(@Path("linkId") linkId: String)
}
