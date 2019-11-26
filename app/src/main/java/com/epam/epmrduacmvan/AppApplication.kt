package com.epam.epmrduacmvan

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.epam.epmrduacmvan.Constants.Companion.SHARED_PREF
import com.epam.epmrduacmvan.Constants.Companion.USER_TOKEN

class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        preferences = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        token = sharedPreferences.getString(USER_TOKEN, "") ?: ""
    }

    companion object {
        private lateinit var preferences: SharedPreferences
        var token: String = ""

        val sharedPreferences: SharedPreferences
            get() = preferences
    }
}