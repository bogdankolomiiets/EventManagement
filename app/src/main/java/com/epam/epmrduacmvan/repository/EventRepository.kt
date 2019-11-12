package com.epam.epmrduacmvan.repository

import com.epam.epmrduacmvan.model.Event

interface EventRepository {
    fun getEvents(): List<Event>
    fun newEvent(event: Event)
    fun goingToAttend(eventId: Int, userEmail: String)
}