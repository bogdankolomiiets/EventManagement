package com.epam.epmrduacmvan.views

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.epam.epmrduacmvan.QueryParameters.Parameters.PAGE
import com.epam.epmrduacmvan.R
import com.epam.epmrduacmvan.adapters.DraftEventPagerAdapter
import com.epam.epmrduacmvan.model.Event
import com.epam.epmrduacmvan.utils.isOnline
import com.epam.epmrduacmvan.utils.showErrorToast
import com.epam.epmrduacmvan.viewmodels.EventsViewModel
import com.google.android.material.tabs.TabLayout

class NewEventActivity : AppCompatActivity() {
    private lateinit var eventsViewModel: EventsViewModel
    private lateinit var saveEventButton: Button
    private lateinit var draftEventTabLayout: TabLayout
    private lateinit var draftEventViewPager: ViewPager
    private lateinit var eventTitle: EditText
    val eventTemplate = Event.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_event)

        setSupportActionBar(findViewById(R.id.new_event_tool_bar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        eventTitle = findViewById(R.id.event_title)
        eventTitle.doAfterTextChanged { eventTemplate.title = it.toString() }

        draftEventViewPager = findViewById(R.id.draft_event_view_pager)
        draftEventViewPager.adapter = DraftEventPagerAdapter(supportFragmentManager, listOf(DraftEventInfoFragment(), DraftEventScheduleFragment(), DraftEventSpeakersFragment(), DraftEventParticipantsFragment()))
        draftEventTabLayout = findViewById(R.id.draft_event_tab_layout)
        draftEventTabLayout.setupWithViewPager(draftEventViewPager)

        saveEventButton = findViewById(R.id.save_event_button)
        saveEventButton.setOnClickListener {
            if (isOnline(this)) {
                val event = eventTemplate.build()
                if (event == null) {
                    showErrorToast(getString(R.string.fill_information))
                } else {
                    eventsViewModel.newEvent(event)
                }
            }
        }

        eventsViewModel = MainActivity.obtainViewModel(this)
        eventsViewModel.draftEvent.observe(this, Observer {
            when (it.status) {
                Event.STATUS_DRAFT -> {
                    saveEventButton.setText(R.string.publish)
                    saveEventButton.setBackgroundResource(R.drawable.publish_btn_background)
                    saveEventButton.setOnClickListener { _ ->
                        it.status = Event.STATUS_PUBLISHED
                        eventsViewModel.newEvent(it)
                    }
                }
                Event.STATUS_PUBLISHED -> {
                    eventsViewModel.isRefreshed.postValue(true)
                    eventsViewModel.queryMap[PAGE] = 0
                    eventsViewModel.getEvents()
                    eventsViewModel.draftEvent.value?.status = ""
                    finish()
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.clear_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.clear_search, android.R.id.home -> onBackPressed()
        }
        return true
    }

    override fun onBackPressed() {
        eventsViewModel.draftEvent.value?.status = ""
        super.onBackPressed()
    }
}
