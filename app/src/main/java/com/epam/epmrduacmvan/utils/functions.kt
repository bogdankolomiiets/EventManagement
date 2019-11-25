package com.epam.epmrduacmvan.utils

import android.content.Context
import android.os.SystemClock
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.epam.epmrduacmvan.R
import com.google.android.material.snackbar.Snackbar

fun showCustomSnack(view: View, intValue: Int) {
    val snack = Snackbar.make(view, intValue, Snackbar.LENGTH_LONG)
    snack.config()
    snack.show()
}

fun Snackbar.config(){
    val params = view.layoutParams as ViewGroup.MarginLayoutParams
    params.setMargins(16, 0, 16, 16)
    view.layoutParams = params
    view.setPadding(16, 16, 0, 16)
    ViewCompat.setElevation(view, 6F)
}

fun showCustomToastNoInternet(view: View){
    val customToast = Toast.makeText(view.context, R.string.no_internet_connection, Toast.LENGTH_LONG)
    customToast.setGravity(Gravity.FILL_HORIZONTAL + Gravity.TOP, 0, 0)
    customToast.view = LayoutInflater.from(view.context).inflate(R.layout.custom_toast, view.findViewById(R.id.custom_toast_container))
    customToast.show()
}

fun showSplashScreen(activity: AppCompatActivity) {
    //timer for splash screen
    SystemClock.sleep(400)
    //setting default theme
    activity.setTheme(R.style.WhiteAppTheme)
}

fun hideKeyboard(view: View) {
    val inputManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun showKeyboard(view: View) {
    val inputManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.showSoftInput(view, 0)
}