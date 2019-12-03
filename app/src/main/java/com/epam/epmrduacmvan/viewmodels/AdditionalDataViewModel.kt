package com.epam.epmrduacmvan.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.epam.epmrduacmvan.model.Category
import com.epam.epmrduacmvan.model.City
import com.epam.epmrduacmvan.model.Language
import com.epam.epmrduacmvan.retrofit.AdditionalDataService
import com.epam.epmrduacmvan.retrofit.RetrofitInstance
import com.epam.epmrduacmvan.utils.showErrorToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdditionalDataViewModel: ViewModel() {
    private val retrofit = RetrofitInstance.retrofit
    private val additionalDataService: AdditionalDataService
    val cities = MutableLiveData<List<City>>()
    val language = MutableLiveData<List<Language>>()
    val category = MutableLiveData<List<Category>>()

    init {
        additionalDataService = retrofit.create(AdditionalDataService::class.java)

        getCity()
        getLanguage()
        getCategory()
    }

    private fun getCity() {
        additionalDataService.getCity().enqueue(object : Callback<List<City>>{
            override fun onFailure(call: Call<List<City>>?, t: Throwable) {
                showErrorToast("Failure: ${t.message}")
            }

            override fun onResponse(call: Call<List<City>>?, response: Response<List<City>>) {
                if (!response.isSuccessful) {
                    showErrorToast("Failure: ${response.code()}")
                    return
                }
                cities.postValue(response.body())
            }
        })
    }

    private fun getLanguage() {
        additionalDataService.getLanguage().enqueue(object : Callback<List<Language>>{
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
        additionalDataService.getCategory().enqueue(object : Callback<List<Category>>{
            override fun onFailure(call: Call<List<Category>>?, t: Throwable) {
                showErrorToast("Failure: ${t.message}")
            }

            override fun onResponse(call: Call<List<Category>>?, response: Response<List<Category>>) {
                if (!response.isSuccessful) {
                    showErrorToast("Failure: ${response.code()}")
                    return
                }
                category.postValue(response.body())
            }
        })
    }
}