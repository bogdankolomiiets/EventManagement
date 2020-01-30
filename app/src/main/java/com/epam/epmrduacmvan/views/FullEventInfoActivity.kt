package com.epam.epmrduacmvan.views

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.epam.epmrduacmvan.Constants.Companion.EVENT
import com.epam.epmrduacmvan.R
import com.epam.epmrduacmvan.adapters.FullEventInfoPagerAdapter
import com.epam.epmrduacmvan.databinding.ActivityFullEventInfoBinding
import com.epam.epmrduacmvan.interfaces.EventShareable
import com.epam.epmrduacmvan.model.Event
import com.epam.epmrduacmvan.viewmodels.EventsViewModel
import com.google.android.material.tabs.TabLayout

class FullEventInfoActivity: AppCompatActivity(), EventShareable {
    private lateinit var binding: ActivityFullEventInfoBinding
    private lateinit var fullEventInfoViewpager: ViewPager
    private lateinit var fullEventInfoTabLayout: TabLayout
    private lateinit var eventsViewModel: EventsViewModel
    private lateinit var registerEventButton: Button
    private lateinit var eventForDisplay: Event
    private val DEFAULT_EVENT_ID = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_full_event_info)

        setSupportActionBar(binding.fullEventInfoToolBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fullEventInfoViewpager = findViewById(R.id.full_event_info_viewpager)
        fullEventInfoTabLayout = findViewById(R.id.full_event_info_tab_layout)
        fullEventInfoViewpager.adapter = FullEventInfoPagerAdapter(supportFragmentManager, listOf(FullEventInfoMainFragment(),
            FullEventInfoScheduleFragment(), FullEventInfoSpeakersFragment(), FullEventInfoFilesFragment(), FullEventInfoSponsorsFragment()))
        fullEventInfoTabLayout.setupWithViewPager(fullEventInfoViewpager)

        eventsViewModel = MainActivity.obtainViewModel(this)

        val eventId = intent.getIntExtra(EVENT, DEFAULT_EVENT_ID)
        if (eventId > DEFAULT_EVENT_ID) {
            eventsViewModel.getFullEventInfo(eventId)
        }

        eventsViewModel.eventById.observe(this, Observer {
            binding.event = it
            eventForDisplay = it
        })

        registerEventButton = findViewById(R.id.register_event_button)
        registerEventButton.setOnClickListener {
            when (eventForDisplay.attendeeType) {
                Event.ATTENDEE -> eventsViewModel.doNotWantToAttend(eventForDisplay.id)
                Event.ORGANIZER -> { /* stub */ }
                else -> eventsViewModel.goingToAttend(eventForDisplay.id)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.full_event_info_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.close, android.R.id.home -> onBackPressed()
        }
        return true
    }

    override fun getEvent(): Event {
        return eventForDisplay
    }
}