package com.epam.epmrduacmvan.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.epam.epmrduacmvan.AppApplication
import com.epam.epmrduacmvan.Constants.Companion.YOUTUBE_API_KEY
import com.epam.epmrduacmvan.Constants.Companion.YOUTUBE_REQUEST_PART
import com.epam.epmrduacmvan.R
import com.epam.epmrduacmvan.UrlConstants
import com.epam.epmrduacmvan.UrlConstants.Companion.YOUTUBE_VIDEO_INFO_CONTROLLER
import com.epam.epmrduacmvan.model.*
import com.epam.epmrduacmvan.retrofit.AdditionalDataService
import com.epam.epmrduacmvan.retrofit.RetrofitInstance
import com.epam.epmrduacmvan.utils.showErrorToast
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*
import kotlin.collections.List
import kotlin.collections.MutableMap
import kotlin.collections.filter
import kotlin.collections.mutableMapOf
import kotlin.collections.set

class AdditionalDataViewModel: ViewModel() {
    private val okHttpClient = OkHttpClient.Builder().build()
    private val retrofit = RetrofitInstance.retrofit
    private val additionalDataService: AdditionalDataService
    private val defaultLocale = Locale.getDefault()
    private var youTubeQueryMap: MutableMap<String, String> = mutableMapOf()
    val cityName: MutableLiveData<String> = MutableLiveData()
    val categoryName: MutableLiveData<String> = MutableLiveData()
    val countries = MutableLiveData<List<Country>>()
    val speakers = MutableLiveData<List<Speakers>>()
    val user = MutableLiveData<User>()

    val cities = MutableLiveData<List<City>>()
    fun getCities(): LiveData<List<City>> {
        return Transformations.switchMap(cityName) { input-> MutableLiveData<List<City>>(
            cities.value?.filter { city -> city.name.toLowerCase(defaultLocale).matches(Regex("^.*" + input.toLowerCase(defaultLocale) + ".*$"))})
        }
    }

    val categories = MutableLiveData<List<Category>>()
    fun getCategories(): LiveData<List<Category>> {
        return Transformations.switchMap(categoryName) { input-> MutableLiveData<List<Category>>(
            categories.value?.filter { category -> category.name.toLowerCase(defaultLocale).matches(Regex("^.*" + input.toLowerCase(defaultLocale) + ".*$"))})
        }
    }

    val language = MutableLiveData<List<Language>>()
    fun getLanguages(): LiveData<List<Language>> {
        return language
    }

    private val youtubeVideos = MutableLiveData<YoutubeVideos>()
    fun getYoutubeVideos(): LiveData<YoutubeVideos> {
        return youtubeVideos
    }

    init {
        additionalDataService = retrofit.create(AdditionalDataService::class.java)
        getCity()
        getCountry()
        getLanguage()
        getCategory()
        getUser()
        getSpeakers()
    }

    private fun getCity() {
        additionalDataService.getCity().enqueue(object : Callback<List<City>> {
            override fun onFailure(call: Call<List<City>>?, t: Throwable) {
                showErrorToast("Failure: ${t.message}")
            }

            override fun onResponse(call: Call<List<City>>?, response: Response<List<City>>) {
                if (!response.isSuccessful) {
                    showErrorToast("Failure: ${response.code()}")
                    return
                }
                cities.postValue(response.body())

                //set initial value for display all cities
                cityName.postValue("")
            }
        })
    }

    private fun getCountry() {
        additionalDataService.getCountry().enqueue(object : Callback<List<Country>> {
            override fun onFailure(call: Call<List<Country>>?, t: Throwable) {
                showErrorToast("Failure: ${t.message}")
            }

            override fun onResponse(call: Call<List<Country>>?, response: Response<List<Country>>) {
                if (!response.isSuccessful) {
                    showErrorToast("Failure: ${response.code()}")
                    return
                }
                countries.postValue(response.body())
            }
        })
    }

    private fun getLanguage() {
        additionalDataService.getLanguage().enqueue(object : Callback<List<Language>> {
            override fun onFailure(call: Call<List<Language>>?, t: Throwable) {
                showErrorToast("Failure: ${t.message}")
            }

            override fun onResponse(call: Call<List<Language>>?, response: Response<List<Language>>) {
                if (!response.isSuccessful) {
                    showErrorToast("Failure: ${response.code()}")
                    return
                }
                language.postValue(response.body())
            }
        })
    }

    private fun getCategory() {
        additionalDataService.getCategory().enqueue(object : Callback<List<Category>> {
            override fun onFailure(call: Call<List<Category>>?, t: Throwable) {
                showErrorToast("Failure: ${t.message}")
            }

            override fun onResponse(call: Call<List<Category>>?, response: Response<List<Category>>) {
                if (!response.isSuccessful) {
                    showErrorToast("Failure: ${response.code()}")
                    return
                }
                categories.postValue(response.body())

                //set initial value for display all category
                categoryName.postValue("")
            }
        })
    }

    private fun getSpeakers() {
        additionalDataService.getSpeakers("*").enqueue(object : Callback<List<Speakers>> {
            override fun onFailure(call: Call<List<Speakers>>?, t: Throwable) {
                showErrorToast("Failure: ${t.message}")
            }

            override fun onResponse(call: Call<List<Speakers>>?, response: Response<List<Speakers>>) {
                if (!response.isSuccessful) {
                    showErrorToast("Failure: ${response.code()}")
                    return
                }
                speakers.postValue(response.body())
            }
        })
    }

    fun getUser() {
        additionalDataService.getUser().enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>?, t: Throwable) {
                showErrorToast("Failure: ${t.message}")
            }

            override fun onResponse(call: Call<User>?, response: Response<User>) {
                if (!response.isSuccessful) {
                    showErrorToast("Failure: ${response.code()}")
                    return
                }
                user.postValue(response.body())
            }
        })
    }

    fun addLinkToEvent(eventId: String, artifactDto: Artifact) {
        val jsonObject = JsonObject()
        jsonObject.addProperty("artifactUrl", artifactDto.artifactUrl)
        jsonObject.addProperty("fileName", artifactDto.fileName)
        jsonObject.addProperty("fileSize", artifactDto.fileSize)
        jsonObject.addProperty("id", artifactDto.id)
        jsonObject.addProperty("link", artifactDto.link)

        additionalDataService.addLinkToEvent(eventId, jsonObject).enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>?, t: Throwable) {
                showErrorToast("Failure: ${t.message}")
            }

            override fun onResponse(call: Call<Void>?, response: Response<Void>) {
                if (!response.isSuccessful) {
                    showErrorToast("Failure: ${response.code()}")
                    return
                } else {
                    showErrorToast(AppApplication.appContext.getString(R.string.link_added))
                }
            }
        })
    }

    fun getVideoInfo(videoId: String) {
        youTubeQueryMap["id"] = videoId

        val requestBody = MultipartBody.Builder()
            .addFormDataPart("id", videoId)
            .addFormDataPart("key", YOUTUBE_API_KEY)
            .addFormDataPart("part", YOUTUBE_REQUEST_PART)
            .build()

        val request = Request.Builder()
            .get()
            .url(YOUTUBE_VIDEO_INFO_CONTROLLER)
            .build()

        okHttpClient.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                println("***************************** " + e.message)

                //showErrorToast("Failure: ${e.message}")
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (!response.isSuccessful) {
                    println("***************************** " + response.code)

//                    showErrorToast("Failure: ${response.code}")
                    return
                } else {
                    youtubeVideos.postValue(Gson().fromJson<YoutubeVideos>(response.body?.string().toString(), object : TypeToken<YoutubeVideos>() {}.type))
                }
            }
        })
    }
}