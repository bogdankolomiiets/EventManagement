package com.epam.epmrduacmvan.viewmodels

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.epam.epmrduacmvan.AppApplication
import com.epam.epmrduacmvan.Constants.Companion.YOUTUBE_API_KEY
import com.epam.epmrduacmvan.Constants.Companion.YOUTUBE_REQUEST_PART
import com.epam.epmrduacmvan.R
import com.epam.epmrduacmvan.RequestResponseCodes.Companion.INTERNAL_SERVER_ERROR_500
import com.epam.epmrduacmvan.RequestResponseCodes.Companion.YOUTUBE_LINK_ADDED_OK
import com.epam.epmrduacmvan.RequestResponseCodes.Companion.YOUTUBE_LINK_SERVER_CONTAINS
import com.epam.epmrduacmvan.UrlConstants.Companion.YOUTUBE_VIDEO_INFO_CONTROLLER
import com.epam.epmrduacmvan.model.*
import com.epam.epmrduacmvan.retrofit.AdditionalDataService
import com.epam.epmrduacmvan.retrofit.RetrofitInstance
import com.epam.epmrduacmvan.utils.showErrorToast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.util.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class AdditionalDataViewModel: ViewModel() {
    private val youtubeOkHttpClient = OkHttpClient.Builder().build()
    private val retrofit = RetrofitInstance.retrofit
    private val additionalDataService: AdditionalDataService
    private val defaultLocale = Locale.getDefault()
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

    private val youtubeLinkAddingResult = MutableLiveData<Int>()
    fun getYoutubeLinkAddingResult(): LiveData<Int> {
        return youtubeLinkAddingResult
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
        additionalDataService.addLinkToEvent(eventId, artifactDto).enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>?, t: Throwable) {
                showErrorToast("Failure: ${t.message}")
            }

            override fun onResponse(call: Call<Void>?, response: Response<Void>) {
                if (!response.isSuccessful) {
                    if (response.code() == INTERNAL_SERVER_ERROR_500) {
                        youtubeLinkAddingResult.postValue(YOUTUBE_LINK_SERVER_CONTAINS)
                    } else {
                        showErrorToast("Failure: ${response.code()}")
                    }
                    return
                }
                youtubeLinkAddingResult.postValue(YOUTUBE_LINK_ADDED_OK)
            }
        })
    }

    fun uploadArtifact(eventId: Int, file: File) {
        val filePart = file.readBytes().toRequestBody("*/*".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData("file", file.name, filePart)

        additionalDataService.uploadArtifact(eventId.toString(), multipartBody).enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>?, t: Throwable) {
                showErrorToast("Failure: ${t.message}")
            }

            override fun onResponse(call: Call<Void>?, response: Response<Void>) {
                if (!response.isSuccessful) {
                    showErrorToast("Failure: ${response.code()}")
                    return
                }
                showErrorToast(AppApplication.appContext.getString(R.string.file_uploaded_successful))
            }
        })
    }

    fun getVideoInfo(videoId: String) {
        val requestBody = FormBody.Builder()
            .add("id", videoId)
            .add("key", YOUTUBE_API_KEY)
            .add("part", YOUTUBE_REQUEST_PART)
            .build()

        val request = Request.Builder()
            .method("get", requestBody)
            .url(YOUTUBE_VIDEO_INFO_CONTROLLER)
            .build()

        youtubeOkHttpClient.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Handler(Looper.getMainLooper()).post { showErrorToast("Failure: ${e.message}") }
                call.cancel()
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (!response.isSuccessful) {
                    Handler(Looper.getMainLooper()).post { showErrorToast("Failure: ${response.code}") }
                    return
                }
                youtubeVideos.postValue(Gson().fromJson<YoutubeVideos>(response.body?.string().toString(), object : TypeToken<YoutubeVideos>() {}.type))
            }
        })
    }
}