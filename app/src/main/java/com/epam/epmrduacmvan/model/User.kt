package com.epam.epmrduacmvan.model

data class User (
    val id: Int = 0,
    val firstName: String = "",
    val lastName: String? = "",
    val birthDate: Long?,
    val occupation: Occupation?,
    val company: Company?,
    val city: City?,
    val phoneNumber: String? = "",
    val email: String = "",
    val jobTitle: String = "",
    val photoUrl: String? = "",
    val website: String? = "",
    val summary: String? = "",
    val rating: Int? = 0,
    val favoriteCategories: List<Category>?,
    val favoriteEventsCount: Int?,
    val myEventsCount: Int?
)