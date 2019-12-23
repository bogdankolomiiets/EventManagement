package com.epam.epmrduacmvan.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.lang.Exception

@Parcelize
data class Event internal constructor(
    val id: Int = 0,
    val title: String?,
    val description: String? = "",
    val city: City?,
    val country: Country?,
    val language: Language?,
    val category: Category?,
    val address: String? = "",
    val startDateTime: Long,
    val finishDateTime: Long,
    val eventImageName: String? = "",
    val attachmentFilename: String? = "",
    val eventPhotoUrl: String? = "",
    val url: String? = "",
    val attendeeType: String? = "",
    val type: String?,
    var status: String?,
    val format: String?,
    val speakers: List<Speakers>,
    val labels: String? = "",
    val stage: String?,
    val price: Int?,
    val attendeeCount: Int?
) : Parcelable {
    class Builder {
        var id: Int = 0
        lateinit var title: String
        var description: String = ""
        lateinit var city: City
        var country: Country? = null
        lateinit var language: Language
        lateinit var category: Category
        lateinit var address: String
        var startDateTime: Long = 0
        var finishDateTime: Long = 0
        private var eventImageName: String = ""
        private var attachmentFilename: String = ""
        private var eventPhotoUrl: String = ""
        private var url: String = ""
        private var attendeeType = ""
        var type = TYPE_PUBLIC
        private var status = STATUS_DRAFT
        private var format = FORMAT_OFFLINE
        val speakers: List<Speakers> = listOf()
        private val labels = ""
        private var stage = STAGE_REGISTRATION
        private val price: Int = 0
        private val attendeeCount = 0

        fun build(): Event? {
            return try {
                Event(id, title, description, city, country, language, category, address, startDateTime,
                    finishDateTime, eventImageName, attachmentFilename, eventPhotoUrl, url, attendeeType, type, status, format, speakers, labels, stage, price, attendeeCount)
            } catch (ex: Exception) {
                null
            }
        }
    }

    companion object {
        const val STATUS_DRAFT = "DRAFT"
        const val STATUS_PUBLISHED = "PUBLISHED"
        const val TYPE_PUBLIC = "PUBLIC"
        const val TYPE_PRIVATE = "PRIVATE"
        const val FORMAT_OFFLINE = "OFFLINE"
        const val ATTENDEE = "ATTENDEE"
        const val SPEAKER = "SPEAKER"
        const val ORGANIZER = "ORGANIZER"
        const val STAGE_REGISTRATION = "REGISTRATION"
    }
}