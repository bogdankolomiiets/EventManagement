package com.epam.epmrduacmvan.retrofit

import com.epam.epmrduacmvan.UrlConstants.Companion.CATEGORY_CONTROLLER
import com.epam.epmrduacmvan.UrlConstants.Companion.CITY_CONTROLLER
import com.epam.epmrduacmvan.UrlConstants.Companion.COMPANY_CONTROLLER
import com.epam.epmrduacmvan.UrlConstants.Companion.COUNTRY_CONTROLLER
import com.epam.epmrduacmvan.UrlConstants.Companion.LANGUAGES_CONTROLLER
import com.epam.epmrduacmvan.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface AdditionalDataService {

    @GET(LANGUAGES_CONTROLLER)
    fun getLanguage(): Call<List<Language>>

    @GET(CITY_CONTROLLER)
    fun getCity(): Call<List<City>>

    //reserved
    @GET(CITY_CONTROLLER.plus("/{countryId}"))
    fun getCityByCountry(@Path("countryId") countryId: Int ): Call<List<City>>

    //reserved
    @GET(COUNTRY_CONTROLLER)
    fun getCountry(): Call<List<Country>>

    @GET(CATEGORY_CONTROLLER)
    fun getCategory(): Call<List<Category>>

    @GET(COMPANY_CONTROLLER)
    fun getCompany(): Call<List<Company>>
}