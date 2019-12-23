package com.epam.epmrduacmvan.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Speakers(
    val id: Int,
    val city: City,
    val company: Company,
    val favoriteCategories: List<Category>,
    val firstName: String,
    val lastName: String,
    val jobTitle: String,
    val occupation: Occupation,
    val photoUrl: String,
    val summary: String,
    val website: String
) : Parcelable