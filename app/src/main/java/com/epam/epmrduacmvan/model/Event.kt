package com.epam.epmrduacmvan.model

data class Event(
    val id: Int,
    val title: String,
    val description: String = "",
    val city: City,
    val country: Country,
    val address: String,
    val startDate: Long,
    val finishDate: Long,
//    val tags: Array<String>,
    val eventImageName: String = "",
    val eventImageUrl: String = "",
//    val eventImageData: ByteArray,
    //perhaps ENUM
    val type: String,
    val status: String,
    val format: String
)