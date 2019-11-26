package com.epam.epmrduacmvan

class UrlConstants {
    companion object {
        const val URL: String = "http://35.242.155.84/"
        const val SEND_CODE_CONTROLLER: String = "api/v1/auth/login"
        const val CONFIRM_EMAIL_CONTROLLER: String = "api/v1/auth/confirm"
        const val PASSCODE_CONTROLLER: String = "api/v1/passcode"
        const val EVENTS_CONTROLLER: String = "api/v1/events"
        const val EVENT_ATTENDEE_CONTROLLER: String = EVENTS_CONTROLLER.plus("/attendee")
        const val EVENTS_LIST: String = EVENTS_CONTROLLER.plus("/list")
        const val CITY_CONTROLLER: String = "api/v1/cities"
        const val COUNTRY_CONTROLLER: String = "api/v1/country"

    }
}