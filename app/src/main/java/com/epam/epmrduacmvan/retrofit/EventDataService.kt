package com.epam.epmrduacmvan.retrofit

import com.epam.epmrduacmvan.UrlConstants.Companion.EVENTS_CONTROLLER
import com.epam.epmrduacmvan.model.Event
import com.epam.epmrduacmvan.model.Page
import retrofit2.Call
import retrofit2.http.*

interface EventDataService {

    @GET(EVENTS_CONTROLLER.plus("/list"))
    fun getAllEvents(@QueryMap map: MutableMap<String, Any>): Call<Page>

    @GET(EVENTS_CONTROLLER.plus("/featured"))
    fun getFeaturedEvents(@Query("count") count: Int): Call<List<Event>>

    @GET(EVENTS_CONTROLLER.plus("/{id}"))
    fun getEventById(@Path("id") id: Int): Call<Event>

    @PUT(EVENTS_CONTROLLER.plus("/{id}"))
    fun updateEvent(@Path("id") id: Int, @Body event: Event): Call<Event>

    @POST(EVENTS_CONTROLLER)
    fun newEvent(@Body event: Event)

    @DELETE(EVENTS_CONTROLLER.plus("/{eventId}"))
    fun removeEvent(@Path("eventId") eventId: Int)

    @POST(EVENTS_CONTROLLER.plus("/attendee/{id}"))
    fun goingToAttend(@Path("id") id: Int)

    @DELETE(EVENTS_CONTROLLER.plus("/attendee/{id}"))
    fun doNotWantToAttend(@Path("id") id: Int)

    @PUT(EVENTS_CONTROLLER.plus("/status/{eventId}"))
    fun changeEventStatus(@Path("eventId") eventId: Int)
}