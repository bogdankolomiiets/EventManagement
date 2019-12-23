package com.epam.epmrduacmvan.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CalendarDays(val allCount: Int, val date: String, val filteredCount: Int): Parcelable