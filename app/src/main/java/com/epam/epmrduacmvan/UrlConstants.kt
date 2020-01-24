package com.epam.epmrduacmvan

class UrlConstants {
    companion object {
        const val BASE_URL: String = "http://35.242.155.84:7070"
        const val SEND_CODE_CONTROLLER: String = "/api/v1/auth/login"
        const val CONFIRM_EMAIL_CONTROLLER: String = "/api/v1/auth/confirm"
        const val PASSCODE_CONTROLLER: String = "/api/v1/passcode"
        const val EVENTS_CONTROLLER: String = "/api/v1/events"
        const val PARTICIPANTS_CONTROLLER: String = "/api/v1/participants"
        const val CITY_CONTROLLER: String = "/api/v1/cities"
        const val COUNTRY_CONTROLLER: String = "/api/v1/countries"
        const val CATEGORY_CONTROLLER: String = "/api/v1/categories"
        const val LANGUAGES_CONTROLLER: String = "/api/v1/languages"
        const val ROUND_IMAGE_CONTROLLER: String = "/smallAndRound/"
        const val SPEAKERS_CONTROLLER: String = "/api/v1/speakers"
        const val USER_PROFILE_CONTROLLER: String = "/api/v1/profile/info"
        const val CALENDAR_CONTROLLER: String = "/api/v1/events/calendar"
        const val LINK_CONTROLLER: String = "/api/v1/link"
        const val ARTIFACT_CONTROLLER: String = "/api/v1/artifact"
        const val YOUTUBE_VIDEO_INFO_CONTROLLER: String = "https://www.googleapis.com/youtube/v3/videos"
    }
}