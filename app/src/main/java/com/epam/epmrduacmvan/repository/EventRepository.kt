package com.epam.epmrduacmvan.repository

import androidx.lifecycle.MutableLiveData
import com.epam.epmrduacmvan.model.Event

interface EventRepository {
    fun getAllEvents(events: MutableLiveData<List<Event>>)
    fun getFeaturedEvents(featuredEvents: MutableLiveData<List<Event>>)
    fun getEventById(value: Int, event: MutableLiveData<Event>)
    fun newEvent(event: Event, events: MutableLiveData<List<Event>>)
    fun removeEvent(eventId: Int, events: MutableLiveData<List<Event>>)
    fun goingToAttend(eventId: Int)
    fun doNotWantToAttend(eventId: Int)
}