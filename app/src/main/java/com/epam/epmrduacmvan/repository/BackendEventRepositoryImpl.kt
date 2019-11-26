package com.epam.epmrduacmvan.repository

import androidx.lifecycle.MutableLiveData
import com.epam.epmrduacmvan.AppApplication
import com.epam.epmrduacmvan.Constants.Companion.TOKEN_HEADER_NAME
import com.epam.epmrduacmvan.RequestResponseCodes.Companion.INTERNAL_SERVER_ERROR_500
import com.epam.epmrduacmvan.RequestResponseCodes.Companion.OK_200
import com.epam.epmrduacmvan.UrlConstants.Companion.EVENTS_CONTROLLER
import com.epam.epmrduacmvan.UrlConstants.Companion.EVENTS_LIST
import com.epam.epmrduacmvan.UrlConstants.Companion.URL
import com.epam.epmrduacmvan.model.Event
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONObject
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

object BackendEventRepositoryImpl: EventRepository {
    private var okHttpClient: OkHttpClient = OkHttpClient()
    private lateinit var request: Request
    private lateinit var httpUrl: HttpUrl
    private lateinit var jsonObject: JSONObject
    private lateinit var requestBody: RequestBody
    private val mediaType = "application/json; charset=utf-8".toMediaType()

    override fun getAllEvents(events: MutableLiveData<List<Event>>) {
        request = Request.Builder()
            .url(URL.plus(EVENTS_LIST))
            .get()
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                println(INTERNAL_SERVER_ERROR_500)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.code == OK_200) {
                    try {
                        val string = "[{\"address\":\"Ovodova, 51\",\"attachmentFilename\":\"\",\"category\":{\"id\":1,\"name\":\"JAVA\"},\"city\":{\"id\":1,\"name\":\"Vinnitsya\"},\"country\":{\"id\":1,\"name\":\"Ukraine\"},\"description\":\"description\",\"eventImageName\":\"\",\"eventImageUrl\":\"\",\"finishDate\":1574680659843,\"format\":\"online\",\"language\":{\"id\":1,\"name\":\"UKR\"},\"startDate\":1574680659843,\"status\":\"open\",\"title\":\"Java meetUp\",\"type\":\"type\",\"url\":\"\"}]"
                        val gson = GsonBuilder().create()
                        val eventsList: List<Event> = gson.fromJson(string, object : TypeToken<List<Event>>() {}.type)
                        events.postValue(eventsList)
                        println("HOORAY")
                    } catch (ex: Exception) {
                        println(ex.message)
                    }
                } else println(response.code)
            }
        })
    }

    override fun getFeaturedEvents(featuredEvents: MutableLiveData<List<Event>>) {}

    override fun getEventById(value: Int, event: MutableLiveData<Event>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun newEvent(event: Event, events: MutableLiveData<List<Event>>) {
        requestBody = Gson().toJson(event, Event::class.java).toRequestBody(mediaType)

        request = Request.Builder()
            .url(URL.plus(EVENTS_CONTROLLER))
            .addHeader(TOKEN_HEADER_NAME, AppApplication.token)
            .method("POST", requestBody)
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                println(INTERNAL_SERVER_ERROR_500)
            }

            override fun onResponse(call: Call, response: Response) {
                println(response.code)
            }
        })
    }

    override fun removeEvent(value: Int, events: MutableLiveData<List<Event>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun goingToAttend(eventId: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun doNotWantToAttend(eventId: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}