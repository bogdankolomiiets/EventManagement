package com.epam.epmrduacmvan.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Occupation (val id: Int, val name: String) : Parcelable