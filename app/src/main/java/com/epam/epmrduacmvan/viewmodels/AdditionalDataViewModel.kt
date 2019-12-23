package com.epam.epmrduacmvan.viewmodels

import androidx.lifecycle.*
import com.epam.epmrduacmvan.model.Category
import com.epam.epmrduacmvan.model.City
import com.epam.epmrduacmvan.model.Language
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.epam.epmrduacmvan.model.*
import com.epam.epmrduacmvan.retrofit.AdditionalDataService
import com.epam.epmrduacmvan.retrofit.RetrofitInstance
import com.epam.epmrduacmvan.utils.showErrorToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class AdditionalDataViewModel: ViewModel() {
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
}