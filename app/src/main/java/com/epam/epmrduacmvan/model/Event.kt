package com.epam.epmrduacmvan.model

data class Event(
    val id: Int?,
    val title: String,
    val description: String = "",
    val city: City,
    val country: Country,
    val language: Language,
    val category: Category,
    val address: String,
    val startDate: Long,
    val finishDate: Long,
    val eventImageName: String = "",
    val attachmentFilename: String = "",
    val eventImageUrl: String = "",
    val url: String = "",
    val type: String,
    val status: String,
    val format: String
)