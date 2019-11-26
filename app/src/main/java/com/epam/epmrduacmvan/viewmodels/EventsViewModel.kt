package com.epam.epmrduacmvan.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.epam.epmrduacmvan.model.Event
import com.epam.epmrduacmvan.repository.EventRepository
import com.epam.epmrduacmvan.repository.RepositoryProvider

class EventsViewModel(application: Application) : AndroidViewModel(application){
    private var events: MutableLiveData<List<Event>> = MutableLiveData()
    private var featuredEvents: MutableLiveData<List<Event>> = MutableLiveData()
    private val eventRepository: EventRepository = RepositoryProvider.eventRepository

    init {
        eventRepository.getAllEvents(events)
        eventRepository.getFeaturedEvents(featuredEvents)
    }

    fun getFeaturedEvents(): LiveData<List<Event>>{
        return featuredEvents
    }

    fun getEvents(): LiveData<List<Event>>{
        return events
    }

    fun newEvent(event: Event) {
        eventRepository.newEvent(event, events)
    }
}