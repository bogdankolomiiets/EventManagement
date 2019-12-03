package com.epam.epmrduacmvan.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import com.epam.epmrduacmvan.Constants.Companion.EMAIL
import com.epam.epmrduacmvan.Constants.Companion.PASS_CODE
import com.epam.epmrduacmvan.Constants.Companion.SHARED_PREF
import com.epam.epmrduacmvan.Constants.Companion.USER_TOKEN
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.epam.epmrduacmvan.QueryParameters.Parameters.PAGE
import com.epam.epmrduacmvan.R
import com.epam.epmrduacmvan.adapters.EventRecyclerViewAdapter
import com.epam.epmrduacmvan.databinding.ActivityMainBinding
import com.epam.epmrduacmvan.model.Page
import com.epam.epmrduacmvan.utils.PaginationScrollListener
import com.epam.epmrduacmvan.viewmodels.EventsViewModel
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var eventsViewModel: EventsViewModel
    private lateinit var eventRecyclerViewAdapter: EventRecyclerViewAdapter
    private lateinit var eventsRecycler: RecyclerView
    private lateinit var eventsPage: Page
    private lateinit var noEventsFound: View
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        noEventsFound = binding.noEventsFound

        setupRecyclerViewAndAdapter()

        eventsViewModel = ViewModelProviders.of(this).get(EventsViewModel::class.java)
        eventsViewModel.events.observe(this, Observer {
            noEventsFound.isVisible = it.content.isEmpty()
            binding.eventLoadingProgressbar.visibility = View.INVISIBLE
            eventRecyclerViewAdapter.addData(it.content)
            eventsPage = it
        })

        eventsViewModel.featuredEvents.observe(this, Observer {
            it.forEach { println(it) }
        })

        drawerLayout = binding.drawerLayout
        drawer_navigation_view.setNavigationItemSelectedListener(this)
        setupActionBar()
    }

    private fun setupRecyclerViewAndAdapter() {
        eventsRecycler = binding.eventsRecycler
        eventsRecycler.setHasFixedSize(true)

        // Creates a vertical Layout Manager
        val layoutManager = LinearLayoutManager(this)
        eventsRecycler.layoutManager = layoutManager

        eventsRecycler.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun isLastPage(): Boolean {
                return eventsPage.last
            }

            override fun loadMoreItems() {
                binding.eventLoadingProgressbar.visibility = View.VISIBLE
                eventsViewModel.queryMap[PAGE] = eventsPage.number.inc()
                eventsViewModel.getEvents()
            }
        })

        eventRecyclerViewAdapter = EventRecyclerViewAdapter()
        eventsRecycler.adapter = eventRecyclerViewAdapter
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.mainToolBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        drawerToggle = ActionBarDrawerToggle(
            this, drawerLayout, binding.mainToolBar,
            R.string.open,
            R.string.close
        )
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    //onClick for "sign out" menu item
    fun signOut(item: MenuItem) {
        getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
            .edit()
            .remove(EMAIL)
            .remove(PASS_CODE)
            .remove(USER_TOKEN)
            .apply()

        startActivity(Intent(this, StartActivity::class.java))
        finish()
    }

    fun search(item: MenuItem) {}
    fun filter(item: MenuItem) {}


    fun addNewEvent(view: View) {}
}