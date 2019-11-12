package com.epam.epmrduacmvan.utils

import android.os.SystemClock
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.epam.epmrduacmvan.R
import com.google.android.material.snackbar.Snackbar

fun showSnackBar(view: View, intValue: Int) {
    Snackbar.make(view, intValue, Snackbar.LENGTH_LONG).show()
}

fun showSplashScreen(activity: AppCompatActivity) {
    //timer for splash screen
    SystemClock.sleep(400)
    //setting default theme
    activity.setTheme(R.style.WhiteAppTheme)
}