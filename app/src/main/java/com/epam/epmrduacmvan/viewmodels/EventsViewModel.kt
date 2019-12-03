package com.epam.epmrduacmvan.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.epam.epmrduacmvan.Constants.Companion.FEATURED_EVENT_COUNT
import com.epam.epmrduacmvan.QueryParameters.Parameters.PAGE
import com.epam.epmrduacmvan.QueryParameters.Parameters.SIZE
import com.epam.epmrduacmvan.QueryParameters.Parameters.SORT
import com.epam.epmrduacmvan.model.Event
import com.epam.epmrduacmvan.model.Page
import com.epam.epmrduacmvan.retrofit.EventDataService
import com.epam.epmrduacmvan.retrofit.RetrofitInstance
import com.epam.epmrduacmvan.utils.showErrorToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventsViewModel : ViewModel() {
    private val retrofit = RetrofitInstance.retrofit
    private val eventDataService: EventDataService
    var queryMap: MutableMap<String, Any> = mutableMapOf()
    var gotError: MutableLiveData<Boolean> = MutableLiveData()

    private var mEventsPage: MutableLiveData<Page> = MutableLiveData()
    val events: LiveData<Page>
        get() = mEventsPage

    private var mFeaturedEvents: MutableLiveData<List<Event>> = MutableLiveData()
    val featuredEvents: LiveData<List<Event>>
    get() = mFeaturedEvents

    init {
        eventDataService = retrofit.create(EventDataService::class.java)

        //init base query
        queryMap[PAGE] = 0
        queryMap[SIZE] = 20
        queryMap[SORT] = "startDateTime"

        getFeaturedEvents()
        getEvents()
    }

    private fun getFeaturedEvents(){
        eventDataService.getFeaturedEvents(FEATURED_EVENT_COUNT).enqueue(object: Callback<List<Event>> {
            override fun onFailure(call: Call<List<Event>>?, t: Throwable) {
                gotError.postValue(true)
                showErrorToast("Failure: ${t.message}")
            }

            override fun onResponse(call: Call<List<Event>>?, response: Response<List<Event>>) {
                if (!response.isSuccessful) {
                    gotError.postValue(true)
                    showErrorToast("Failure: ${response.code()}")
                    return
                }
                gotError.postValue(false)
                mFeaturedEvents.postValue(response.body())
            }
        })
    }

    fun getEvents(){
        eventDataService.getAllEvents(queryMap).enqueue(object: Callback<Page> {
            override fun onFailure(call: Call<Page>?, t: Throwable) {
                gotError.postValue(true)
                showErrorToast("Failure: ${t.message}")
            }

            override fun onResponse(call: Call<Page>?, response: Response<Page>) {
                if (!response.isSuccessful) {
                    gotError.postValue(true)
                    showErrorToast("Failure: ${response.code()}")
                    return
                }
                gotError.postValue(false)
                mEventsPage.postValue(response.body())
            }
        })
    }

    fun newEvent(event: Event) {
        //todo
    }
}