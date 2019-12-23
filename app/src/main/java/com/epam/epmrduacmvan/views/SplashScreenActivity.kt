package com.epam.epmrduacmvan.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.epam.epmrduacmvan.AppApplication
import com.epam.epmrduacmvan.Constants.Companion.BOOL_EXTRA
import com.epam.epmrduacmvan.R
import com.epam.epmrduacmvan.utils.isOnline
import com.epam.epmrduacmvan.viewmodels.AdditionalDataViewModel
import com.epam.epmrduacmvan.viewmodels.EventsViewModel

class SplashScreenActivity: AppCompatActivity() {
    private lateinit var eventsViewModel: EventsViewModel
    private lateinit var retryButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        retryButton = findViewById(R.id.retry_button)
    }

    override fun onStart() {
        super.onStart()
        //getting all we need from server
        getData()
    }

    private fun getData() {
        if (!isOnline(this)) {
            retryButton.isVisible = true
        } else {
            retryButton.isVisible = false
            if (AppApplication.token.isNotEmpty()) {
                //init EventsViewModel
                ViewModelProviders.of(this).get(AdditionalDataViewModel::class.java)
                eventsViewModel = ViewModelProviders.of(this).get(EventsViewModel::class.java)
                eventsViewModel.isEventsLoaded.observe(this, Observer {
                    startActivity(Intent(this, StartActivity::class.java).putExtra(BOOL_EXTRA, it))
                    finish()
                })
            } else {
                startActivity(Intent(this, StartActivity::class.java))
                finish()
            }
        }
    }

    fun retry(view: View) {
        getData()
    }
}