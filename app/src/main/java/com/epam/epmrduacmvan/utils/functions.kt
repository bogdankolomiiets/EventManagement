package com.epam.epmrduacmvan.utils

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.epam.epmrduacmvan.AppApplication
import com.epam.epmrduacmvan.R
import com.epam.epmrduacmvan.UrlConstants
import com.epam.epmrduacmvan.model.Event
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
val dateFormatter = SimpleDateFormat("dd\nMM", Locale.getDefault())
val dateFormatterForEvent = SimpleDateFormat("dd MMM", Locale.getDefault())
val dateFormatterForFullEventInfo = SimpleDateFormat("dd MMM - HH:mm", Locale.getDefault())

fun showCustomSnack(view: View, intValue: Int) {
    val snack = Snackbar.make(view, intValue, Snackbar.LENGTH_LONG)
    snack.config()
    snack.show()
}

fun Snackbar.config() {
    val params = view.layoutParams as ViewGroup.MarginLayoutParams
    params.setMargins(16, 0, 16, 16)
    view.layoutParams = params
    view.setPadding(16, 16, 0, 16)
    ViewCompat.setElevation(view, 6F)
}

fun showCustomToastNoInternet(context: Context) {
    val customToast = Toast.makeText(context, R.string.no_internet_connection, Toast.LENGTH_LONG)
    customToast.setGravity(Gravity.FILL_HORIZONTAL + Gravity.TOP, 0, 0)
    customToast.view = View.inflate(context, R.layout.custom_toast, null)
    customToast.show()
}

fun hideKeyboard(view: View) {
    val inputManager =
        view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun showKeyboard(view: View) {
    val inputManager =
        view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.showSoftInput(view, 0)
}

fun isOnline(context: Context): Boolean {
    val connectivityManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return if (networkInfo != null && networkInfo.isConnected) {
        true
    } else {
        showCustomToastNoInternet(context)
        false
    }
}

fun showErrorToast(message: String) {
    Toast.makeText(AppApplication.appContext, message, Toast.LENGTH_LONG).show()
}

@BindingAdapter("LongToTime")
fun TextView.convertLongToTime(longDate: Long) {
    text = timeFormatter.format(Date(longDate))
}

@BindingAdapter("longToDate")
fun TextView.convertLongToDate(longDate: Long) {
    text = dateFormatter.format(Date(longDate))
}

@BindingAdapter("getImageFromUrl")
fun ImageView.load(url: String?) {
    setBackgroundResource(R.color.colorWaikawaGray)
    Glide.with(AppApplication.appContext)
            .load(UrlConstants.BASE_URL.plus("/").plus(url))
            .error(R.drawable.default_event_image)
            .into(this)
}

@BindingAdapter("longToDateForEvent")
fun TextView.convertLongToDateForEvent(longDate: Long) {
    text = dateFormatterForEvent.format(Date(longDate))
}

@BindingAdapter("longToDateForFullEventInfo")
fun TextView.convertLongToDateForFullEventInfo(longDate: Long) {
    text = dateFormatterForFullEventInfo.format(Date(longDate))
}

@BindingAdapter("eventPriceToString")
fun TextView.eventPriceToString(value: Int?) {
        text = when(value) {
            null, 0 -> resources.getString(R.string.free)
            else -> value.toString()
        }
}

@BindingAdapter("setStyleForRegisterButton")
fun Button.getTextForButton(eventAttendeeType: String?) {
    setBackgroundResource(R.drawable.next_btn_background)
    (when (eventAttendeeType) {
        Event.ATTENDEE -> setText(R.string.cancel_registration)
        Event.ORGANIZER -> {
            setText(R.string.my_event)
            setBackgroundResource(R.drawable.my_event_btn_background)
        }
        else -> setText(R.string.register)
    })
}

@BindingAdapter("OnlineOrOffline")
fun TextView.showOrHide(format: String) {
    text = if (format == "ONLINE") {
        "On-Line"
    } else {
        null
    }
    visibility = if (format == "ONLINE") {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }
}

fun generateFileName(): String {
    return UUID.randomUUID().toString().plus(".jpg")
}