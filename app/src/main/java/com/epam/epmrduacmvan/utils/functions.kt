package com.epam.epmrduacmvan.utils

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.ViewCompat
import com.epam.epmrduacmvan.AppApplication
import com.epam.epmrduacmvan.R
import com.google.android.material.snackbar.Snackbar
import retrofit2.Response

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