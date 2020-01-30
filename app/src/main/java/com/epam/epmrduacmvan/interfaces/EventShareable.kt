package com.epam.epmrduacmvan.interfaces

import com.epam.epmrduacmvan.model.Event

interface EventShareable {
    fun getEvent(): Event
}