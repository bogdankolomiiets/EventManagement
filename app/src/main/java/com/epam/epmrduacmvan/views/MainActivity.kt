package com.epam.epmrduacmvan.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import com.epam.epmrduacmvan.Constants.Companion.EMAIL
import com.epam.epmrduacmvan.Constants.Companion.PASS_CODE
import com.epam.epmrduacmvan.Constants.Companion.SHARED_PREF
import com.epam.epmrduacmvan.Constants.Companion.USER_TOKEN
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.epam.epmrduacmvan.R
import com.epam.epmrduacmvan.adapters.EventRecyclerViewAdapter
import com.epam.epmrduacmvan.databinding.ActivityMainBinding
import com.epam.epmrduacmvan.model.*
import com.epam.epmrduacmvan.viewmodels.EventsViewModel
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var eventsViewModel: EventsViewModel
    private lateinit var eventRecyclerViewAdapter: EventRecyclerViewAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var drawerToggle: ActionBarDrawerToggle

    //for test
    private var testValue = 0
    private lateinit var descriptionTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //for test
        descriptionTextView = findViewById(R.id.description_text)

        eventRecyclerViewAdapter = EventRecyclerViewAdapter()
        eventsViewModel = ViewModelProviders.of(this).get(EventsViewModel::class.java)
        eventsViewModel.getEvents().observe(this, Observer {
            eventRecyclerViewAdapter.setData(it)
        })
        drawerLayout = binding.drawerLayout
        drawer_navigation_view.setNavigationItemSelectedListener(this)
        setupActionBar()
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


    //just for test
    fun addNewEvent(view: View) {
        if (testValue == 0) {
            descriptionTextView.text = "Добавим в предыдущую сцену оба набора ограничений. Они почти одинаковые, только зеркально отражены по обеим сторонам экрана.\n" + "\n" + "Теперь у нас три набора ограничений — start, like и pass. Давайте определим переходы (Transition) между этими состояниями."
            testValue++
        } else {
            testValue = 0
            descriptionTextView.text = "Обратите внимание на эту строку: app:motionDebug=«SHOW_ALL». Она позволяет нам выводить на экран отладочную информацию, траекторию движения объектов, состояния с началом и концом анимации, а также текущий прогресс. Строчка очень помогает при отладке, но не забудьте удалить её, прежде чем отправлять в прод: никакой напоминалки для этого нет."
        }
    }
}