package com.epam.epmrduacmvan.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.epam.epmrduacmvan.AppApplication
import com.epam.epmrduacmvan.Constants
import com.epam.epmrduacmvan.QueryParameters.Parameters.PAGE
import com.epam.epmrduacmvan.QueryParameters.Parameters.SIZE
import com.epam.epmrduacmvan.QueryParameters.Parameters.SORT
import com.epam.epmrduacmvan.R
import com.epam.epmrduacmvan.model.CalendarCount
import com.epam.epmrduacmvan.model.Event
import com.epam.epmrduacmvan.model.Page
import com.epam.epmrduacmvan.retrofit.EventDataService
import com.epam.epmrduacmvan.retrofit.RetrofitInstance
import com.epam.epmrduacmvan.utils.showErrorToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventsViewModel : ViewModel() {
    var queryMap: MutableMap<String, Any> = mutableMapOf()
    private val retrofit = RetrofitInstance.retrofit
    private val eventDataService: EventDataService

    var isEventsLoaded = MutableLiveData<Boolean>()
    var isRefreshed = MutableLiveData<Boolean>()

    private val mEventsPage = MutableLiveData<Page>()
    val events: LiveData<Page>
        get() = mEventsPage

    private val mFeaturedEvents = MutableLiveData<List<Event>>()
    val featuredEvents: LiveData<List<Event>>
    get() = mFeaturedEvents

    private val mDraftEvent = MutableLiveData<Event>()
    val draftEvent: LiveData<Event>
    get() = mDraftEvent

    val eventById = MutableLiveData<Event>()
    val updatedEvent = MutableLiveData<Event>()
//    val updatedEvent: LiveData<Event>
//        get() = mUpdatedEvent

    private var mEventCalendarCount = MutableLiveData<CalendarCount>()
    val eventCalendarCount: LiveData<CalendarCount>
        get() = mEventCalendarCount

    init {
        eventDataService = retrofit.create(EventDataService::class.java)

        //init base query
        setDefaultQuery()
        getFeaturedEvents()
        getEvents()
    }

    fun setDefaultQuery() {
        queryMap.clear()
        queryMap[PAGE] = 0
        queryMap[SIZE] = 20
        queryMap[SORT] = "startDateTime"
    }

    private fun getFeaturedEvents(){
        eventDataService.getFeaturedEvents(Constants.FEATURED_EVENT_COUNT).enqueue(object: Callback<List<Event>> {
            override fun onFailure(call: Call<List<Event>>?, t: Throwable) {
                showErrorToast("Failure: ${t.message}")
            }

            override fun onResponse(call: Call<List<Event>>?, response: Response<List<Event>>) {
                if (!response.isSuccessful) {
                    showErrorToast("Failure: ${response.code()}")
                    return
                }
                mFeaturedEvents.postValue(response.body())
            }
        })
    }

    fun getEvents() {
        eventDataService.getAllEvents(queryMap).enqueue(object: Callback<Page> {
            override fun onFailure(call: Call<Page>, t: Throwable) {
                showErrorToast("Failure: ${t.message}")
                isEventsLoaded.postValue(false)
                call.cancel()
            }

            override fun onResponse(call: Call<Page>?, response: Response<Page>) {
                if (!response.isSuccessful) {
                    showErrorToast("Failure: ${response.code()}")
                    isEventsLoaded.postValue(false)
                    return
                }
                getCalendarCount(queryMap)
                mEventsPage.postValue(response.body())
                isEventsLoaded.postValue(true)
            }
        })
    }

    fun getFullEventInfo(eventId: Int) {
        eventDataService.getFullEventInfo(eventId.toString()).enqueue(object : Callback<Event> {
            override fun onFailure(call: Call<Event>, t: Throwable) {
                showErrorToast("Failure: ${t.message}")
                call.cancel()
            }

            override fun onResponse(call: Call<Event>?, response: Response<Event>) {
                if (!response.isSuccessful) {
                    showErrorToast("Failure: ${response.code()}")
                    return
                }
                eventById.postValue(response.body())
            }
        })
    }

    fun getEventById(eventId: Int) {
        getFullEventInfo(eventId)
    }

    fun newEvent(event: Event) {
        eventDataService.newEvent(event).enqueue(object : Callback<Event>{
            override fun onFailure(call: Call<Event>?, t: Throwable) {
                showErrorToast("Failure: ${t.message}")
            }

            override fun onResponse(call: Call<Event>?, response: Response<Event>) {
                if (!response.isSuccessful) {
                    showErrorToast("Failure: ${response.code()}")
                    return
                }
                showErrorToast(AppApplication.appContext.getString(R.string.success))
                mDraftEvent.postValue(response.body())
            }
        })
    }

    fun changeEventStatus(event: Event) {
        eventDataService.changeEventStatus(event.id).enqueue(object : Callback<Event>{
            override fun onFailure(call: Call<Event>?, t: Throwable) {
                showErrorToast("Failure: ${t.message}")
            }

            override fun onResponse(call: Call<Event>?, response: Response<Event>) {
                if (!response.isSuccessful) {
                    showErrorToast("Failure: ${response.code()}")
                    return
                }
                mDraftEvent.postValue(response.body())
            }
        })
    }

    fun goingToAttend(eventId: Int) {
        eventDataService.goingToAttend(eventId).enqueue(object : Callback<Event>{
            override fun onFailure(call: Call<Event>?, t: Throwable) {
                showErrorToast("Failure: ${t.message}")
            }

            override fun onResponse(call: Call<Event>?, response: Response<Event>) {
                if (!response.isSuccessful) {
                    showErrorToast("Failure: ${response.code()}")
                    return
                }
                getEventById(eventId)
                updatedEvent.postValue(response.body())
                showErrorToast(AppApplication.appContext.getString(R.string.registered_successfully))
            }
        })
    }

    fun doNotWantToAttend(eventId: Int) {
        eventDataService.doNotWantToAttend(eventId).enqueue(object : Callback<Event>{
            override fun onFailure(call: Call<Event>?, t: Throwable) {
                showErrorToast("Failure: ${t.message}")
            }

            override fun onResponse(call: Call<Event>?, response: Response<Event>) {
                if (!response.isSuccessful) {
                    showErrorToast("Failure: ${response.code()}")
                    return
                }
                getEventById(eventId)
                updatedEvent.postValue(response.body())
                showErrorToast(AppApplication.appContext.getString(R.string.unregistered_successfully))
            }
        })
    }

    private fun getCalendarCount(queryMap: MutableMap<String, Any>) {
        eventDataService.getCalendarCount(queryMap).enqueue(object : Callback<CalendarCount>{
            override fun onFailure(call: Call<CalendarCount>?, t: Throwable) {
                showErrorToast("Failure: ${t.message}")
            }

            override fun onResponse(call: Call<CalendarCount>?, response: Response<CalendarCount>) {
                if (!response.isSuccessful) {
                    showErrorToast("Failure: ${response.code()}")
                    return
                }
                mEventCalendarCount.postValue(response.body())
            }
        })
    }
}