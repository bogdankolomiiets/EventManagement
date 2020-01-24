package com.epam.epmrduacmvan.retrofit

import com.epam.epmrduacmvan.UrlConstants.Companion.CALENDAR_CONTROLLER
import com.epam.epmrduacmvan.UrlConstants.Companion.EVENTS_CONTROLLER
import com.epam.epmrduacmvan.UrlConstants.Companion.PARTICIPANTS_CONTROLLER
import com.epam.epmrduacmvan.model.CalendarCount
import com.epam.epmrduacmvan.model.Event
import com.epam.epmrduacmvan.model.Page
import retrofit2.Call
import retrofit2.http.*

interface EventDataService {

    @GET(EVENTS_CONTROLLER.plus("/search"))
    fun getAllEvents(@QueryMap map: MutableMap<String, Any>): Call<Page>

    @GET(EVENTS_CONTROLLER.plus("/featured"))
    fun getFeaturedEvents(@Query("count") count: Int): Call<List<Event>>

    /*reserved
    *@GET(EVENTS_CONTROLLER.plus("/{eventId}"))
    *fun getEventById(@Path("eventId") eventId: Int): Call<Event>
     */

    @GET(EVENTS_CONTROLLER.plus("/info/{eventId}"))
    fun getFullEventInfo(@Path("eventId") eventId: String): Call<Event>

    /*reserved
    *@PUT(EVENTS_CONTROLLER.plus("/{id}"))
    *fun updateEvent(@Path("id") id: Int, @Body event: Event): Call<Event>
    */

    @POST(EVENTS_CONTROLLER)
    fun newEvent(@Body event: Event): Call<Event>

    @PUT(EVENTS_CONTROLLER.plus("/{eventId}"))
    fun changeEventStatus(@Path( "eventId") eventId: String, @Body event: Event): Call<Void>

    /*reserved
    *@DELETE(EVENTS_CONTROLLER.plus("/{eventId}"))
    *fun removeEvent(@Path("eventId") eventId: Int)
     */

    @POST(PARTICIPANTS_CONTROLLER.plus("/attendee/{id}"))
    fun goingToAttend(@Path("id") id: Int): Call<Event>

    @DELETE(PARTICIPANTS_CONTROLLER.plus("/attendee/{id}"))
    fun doNotWantToAttend(@Path("id") id: Int): Call<Event>

    /*reserved
    *@PUT(EVENTS_CONTROLLER.plus("/status/{eventId}"))
    *fun changeEventStatus(@Path("eventId") eventId: Int): Call<Void>
    */

    @GET(CALENDAR_CONTROLLER)
    fun getCalendarCount(@QueryMap map: MutableMap<String, Any>): Call<CalendarCount>
}