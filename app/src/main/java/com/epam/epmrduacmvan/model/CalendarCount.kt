package com.epam.epmrduacmvan.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CalendarCount(val calendarDays : List<CalendarDays>): Parcelable