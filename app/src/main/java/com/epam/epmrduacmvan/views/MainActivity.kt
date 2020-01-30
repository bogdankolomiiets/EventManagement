package com.epam.epmrduacmvan.views

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.LayoutDirection
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.epam.epmrduacmvan.AppApplication
import com.epam.epmrduacmvan.Constants.Companion.EMAIL
import com.epam.epmrduacmvan.Constants.Companion.EVENT
import com.epam.epmrduacmvan.Constants.Companion.PASS_CODE
import com.epam.epmrduacmvan.Constants.Companion.SHARED_PREF
import com.epam.epmrduacmvan.Constants.Companion.USER_TOKEN
import com.epam.epmrduacmvan.QueryParameters.Parameters.CATEGORY_ID
import com.epam.epmrduacmvan.QueryParameters.Parameters.CITY_ID
import com.epam.epmrduacmvan.QueryParameters.Parameters.COUNTRY_ID
import com.epam.epmrduacmvan.QueryParameters.Parameters.FINISH_DATE
import com.epam.epmrduacmvan.QueryParameters.Parameters.FORMAT
import com.epam.epmrduacmvan.QueryParameters.Parameters.LANGUAGE_ID
import com.epam.epmrduacmvan.QueryParameters.Parameters.ONLY_MY_EVENTS
import com.epam.epmrduacmvan.QueryParameters.Parameters.PAGE
import com.epam.epmrduacmvan.QueryParameters.Parameters.SPEAKERS_ID
import com.epam.epmrduacmvan.QueryParameters.Parameters.START_DATE
import com.epam.epmrduacmvan.R
import com.epam.epmrduacmvan.UrlConstants.Companion.BASE_URL
import com.epam.epmrduacmvan.adapters.*
import com.epam.epmrduacmvan.databinding.ActivityMainBinding
import com.epam.epmrduacmvan.model.*
import com.epam.epmrduacmvan.utils.*
import com.epam.epmrduacmvan.viewmodels.AdditionalDataViewModel
import com.epam.epmrduacmvan.viewmodels.EventsViewModel
import com.google.android.material.navigation.NavigationView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_layout_header.view.*
import kotlinx.android.synthetic.main.pull_for_more_start.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.*
import kotlin.math.roundToInt
import io.reactivex.Observable
import java.util.concurrent.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    EventPopupMenuItemClickListener, EventItemClickListener, CustomCalendarItemClickListener {
    private lateinit var eventsViewModel: EventsViewModel
    private lateinit var additionalDataViewModel: AdditionalDataViewModel
    private lateinit var eventRecyclerViewAdapter: EventRecyclerViewAdapter
    private lateinit var horizontalCalendarAdapter: SimpleCalendarRecyclerViewAdapter
    private lateinit var eventsRecycler: RecyclerView
    private lateinit var eventsLayoutManager: LinearLayoutManager
    private lateinit var horizontalCalendarRecyclerView: RecyclerView
    private lateinit var horizontalCalendarLayoutManager: LinearLayoutManager
    private lateinit var userHeader: LinearLayout
    private lateinit var eventsPage: Page
    private lateinit var noEventsFound: View
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var numberOfResult: TextView
    private val calendar = getInstance()
    private val listOfDays = mutableListOf<Date>()
    private val monthFormat = SimpleDateFormat("MMMM", Locale.getDefault())
    private val matchFormat = SimpleDateFormat("dd.MM", Locale.getDefault())
    private val shift = 1
    private val eventItemOffset = 3
    private var eventsRecyclerCompletelyVisibleItemPosition = -1
    private val intervalDuration = 4000L
    private val minHorizontalCalendarItemsCount = 4

    private var customCalendarItemClickPreviousPosition: Int = 0
    private var isCustomCalendarItemPositionFound: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        noEventsFound = binding.noEventsFound

        val dateStart = getInstance()
        val dateEnd = getInstance()
        dateEnd.add(YEAR, 1)

        additionalDataViewModel = ViewModelProviders.of(this).get(AdditionalDataViewModel::class.java)
        additionalDataViewModel.user.observe(this, Observer {
            userHeader.user_profile_email.text = it?.email
            userHeader.user_profile_name.text = it?.firstName.plus(" ").plus(it.lastName)

            when (it.rating) {
                null, 0 -> userHeader.user_profile_rating.rating = 4F//0F
                else -> userHeader.user_profile_rating.rating = it.rating.toFloat()
            }

            Glide.with(AppApplication.appContext)
                .load(BASE_URL.plus("/").plus(it.photoUrl))
                .error(R.drawable.account_circle)
                .into(userHeader.user_profile_image)

            drawer_navigation_view.menu.findItem(R.id.my_events).title = getString(R.string.my_events, it.myEventsCount ?: 0)
            drawer_navigation_view.menu.findItem(R.id.favourite_events).title = getString(R.string.favourite_events, it.favoriteEventsCount ?: 0)
            })

        eventsViewModel = obtainViewModel(this)
        eventsViewModel.events.observe(this, Observer {
            binding.eventLoadingProgressbar.visibility = View.INVISIBLE
            noEventsFound.isVisible = it.content.isEmpty()
            eventsPage = it
            val isRefreshed = eventsViewModel.isRefreshed.value ?: false
            eventRecyclerViewAdapter.addData(it.content, isRefreshed)
            if (isRefreshed) {
                eventsViewModel.isRefreshed.postValue(false)
            }

            numberOfResult.text = getString(R.string.number_of_results,  it.totalElements)

            if (!isCustomCalendarItemPositionFound && it.content.indexOfFirst { event: Event -> event.startDateTime < listOfDays[customCalendarItemClickPreviousPosition].time } != -1) {
                onItemClick(customCalendarItemClickPreviousPosition)
            }
        })

        eventsViewModel.eventCalendarCount.observe(this, Observer {
            horizontalCalendarAdapter.updateCalendarData(it.calendarDays)
        })

        eventsViewModel.updatedEvent.observe(this, Observer {
            try {
                //updating events count on drawer layout
                additionalDataViewModel.getUser()
                eventRecyclerViewAdapter.refreshEvent(it)
            } catch (ex: Exception) { /* stub */ }

        })

        eventsViewModel.featuredEvents.observe(this, Observer {
            val featuredEventsPagerAdapter = FeaturedEventPagerAdapter(supportFragmentManager)
            featuredEventsPagerAdapter.createFragments(it)
            dot_view_pager.adapter = featuredEventsPagerAdapter
            dot_view_pager.addOnPageChangeListener(FeaturedEventViewPagerListener(this::onDotPageSelected))

            //Alternative code for fake drag
            /*Executors.newSingleThreadExecutor().execute {
                val size = featuredEventsPagerAdapter.count
                var item = dot_view_pager.currentItem

                while (true) {
                    runOnUiThread { dot_view_pager.setCurrentItem(item, true)}
                    item = if (item == size) 0 else item.inc()
                    Thread.sleep(3000)
                }
            }*/

            val compositeDisposable = CompositeDisposable()
            val subscribe = Observable.interval(intervalDuration, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (dot_view_pager.currentItem < featuredEventsPagerAdapter.count - 1) {
                        dot_view_pager.currentItem = dot_view_pager.currentItem + 1
                    } else {
                        dot_view_pager.currentItem = 0
                    }
                }
            compositeDisposable.add(subscribe)
        })

        drawerLayout = binding.drawerLayout
        drawer_navigation_view.setNavigationItemSelectedListener(this)
        userHeader = drawer_navigation_view.getHeaderView(0).findViewById(R.id.drawer_header)

        setupActionBar()
        setupRecyclerViewAndAdapter()
        setupHorizontalCalendarRecyclerView()

        val editSelectCity = findViewById<AutoCompleteTextView>(R.id.edit_select_city)
        val editSelectCountry = findViewById<AutoCompleteTextView>(R.id.edit_select_country)
        val editSelectCategory = findViewById<AutoCompleteTextView>(R.id.edit_select_categories)
        val editSelectLanguage = findViewById<AutoCompleteTextView>(R.id.select_language)
        val editSelectSpeaker = findViewById<AutoCompleteTextView>(R.id.edit_select_speakers)
        val radioGroup = findViewById<RadioGroup>(R.id.radio_group)
        val checkBox = findViewById<CheckBox>(R.id.check_box)
        numberOfResult = findViewById(R.id.text_number_of_result)

        val shakeToClear = findViewById<TextView>(R.id.text_shake_to_clear)
        shakeToClear.setOnClickListener {
            radioGroup.check(R.id.radio_button_all)
            text_select_date_start_filter.text = ""
            text_select_date_end_filter.text = ""
            editSelectCategory.text.clear()
            editSelectCity.text.clear()
            editSelectCountry.text.clear()
            editSelectSpeaker.text.clear()
            editSelectLanguage.text.clear()
            checkBox.isChecked = false
            eventsViewModel.setDefaultQuery()
            eventsViewModel.isRefreshed.postValue(true)
            eventsViewModel.getEvents()
        }

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            var checked = false
            if (checkedId == R.id.radio_button_all) {
                checked = false
            } else if (checkedId == R.id.radio_button_my_events) {
                checked = true
            }
            addFilterParametersWithPageZero(ONLY_MY_EVENTS, checked)
        }

        val calendar = getInstance()
        val currentYear = calendar.get(YEAR)
        val currentMonth = calendar.get(MONTH)
        val currentDay = calendar.get(DAY_OF_MONTH)

        layout_select_date_start_filter.setOnClickListener {
            val dpd = DatePickerDialog(this, R.style.DateTimePickerStyle,
                DatePickerDialog.OnDateSetListener { _, year, month, day ->
                    dateStart.set(year, month, day, 0, 0, 1)

                    if (dateStart.time.after(dateEnd.time)) {
                        text_select_date_start_filter.text = getString(R.string.date_concatenation, day, month.inc(), year)
                        text_select_date_end_filter.text = ""
                        dateEnd.add(YEAR, 1)
                        eventsViewModel.queryMap[FINISH_DATE] = dateEnd.timeInMillis
                    } else {
                        text_select_date_start_filter.text = getString(R.string.date_concatenation, day, month.inc(), year)
                    }
                    addFilterParametersWithPageZero(START_DATE, dateStart.timeInMillis)
                }, currentYear, currentMonth, currentDay)
            dpd.show()
        }

        layout_select_date_end_filter.setOnClickListener {
            val dpd = DatePickerDialog(this, R.style.DateTimePickerStyle,
                DatePickerDialog.OnDateSetListener { _, year, month, day ->
                    dateEnd.set(year, month, day, 23, 59, 59)

                    if (dateEnd.time.after(dateStart.time) || dateEnd.time == dateStart.time) {
                        text_select_date_end_filter.text = getString(R.string.date_concatenation, day, month.inc(), year)
                        eventsViewModel.queryMap[FINISH_DATE] = dateEnd.timeInMillis
                        eventsViewModel.isRefreshed.postValue(true)
                        eventsViewModel.getEvents()
                    } else {
                        Toast.makeText(this, "Select the date that comes after the start date",
                            Toast.LENGTH_LONG).show()
                    }
                }, currentYear, currentMonth, currentDay)
            dpd.show()
        }

        additionalDataViewModel.categories.observe(this, Observer {
            val categoriesAdapter = CategoriesAdapter(this, R.layout.auto_complete_line, it)
            editSelectCategory.setAdapter(categoriesAdapter)
        })
        editSelectCategory.threshold = 1
        editSelectCategory.setOnItemClickListener { parent, _, position, _ ->
            val selectCategory = parent.adapter.getItem(position) as Category?
            if (selectCategory != null) {
                editSelectCategory.setText(selectCategory.name)
                editSelectCategory.setSelection(editSelectCategory.text.length)
                addFilterParametersWithPageZero(CATEGORY_ID, selectCategory.id)
            }
        }
        editSelectCategory.doAfterTextChanged {
            if (it.isNullOrEmpty()) {
                changeFilterParameters(CATEGORY_ID)
            }
        }

        additionalDataViewModel.cities.observe(this, Observer {
            val cityAdapter = CityAdapter(this, R.layout.auto_complete_line_city, it)
            editSelectCity.setAdapter(cityAdapter)
        })
        editSelectCity.threshold = 1
        editSelectCity.setOnItemClickListener { parent, _, position, _ ->
            val selectedCity = parent.adapter.getItem(position) as City?
            if (selectedCity != null) {
                editSelectCity.setText(selectedCity.name)
                editSelectCountry.setText(selectedCity.country?.name)
                editSelectCity.setSelection(editSelectCity.text.length)
                editSelectCity.requestFocus()
                eventsViewModel.queryMap[COUNTRY_ID] = selectedCity.country!!.id
                addFilterParametersWithPageZero(CITY_ID, selectedCity.id)
            }
        }

        editSelectCity.doAfterTextChanged {
            if (it.isNullOrEmpty()) {
                changeFilterParameters(CITY_ID)
            }
        }

        additionalDataViewModel.countries.observe(this, Observer {
            val countryAdapter = CountryAdapter(this, R.layout.auto_complete_line, it)
            editSelectCountry.setAdapter(countryAdapter)
        })
        editSelectCountry.threshold = 1
        editSelectCountry.setOnItemClickListener { parent, _, position, _ ->
            val selectCountry = parent.adapter.getItem(position) as Country?
            if (selectCountry != null) {
                editSelectCity.text.clear()
                editSelectCountry.setText(selectCountry.name)
                editSelectCountry.setSelection(editSelectCountry.text.length)
                editSelectCountry.requestFocus()
                eventsViewModel.queryMap.remove(CITY_ID)
                addFilterParametersWithPageZero(COUNTRY_ID, selectCountry.id)
            }
        }

        editSelectCountry.doAfterTextChanged {
            if (it.isNullOrEmpty()) {
                editSelectCity.text.clear()
                eventsViewModel.queryMap.remove(CITY_ID)
                changeFilterParameters(COUNTRY_ID)
            }
        }

        additionalDataViewModel.language.observe(this, Observer {
            val languageAdapter = LanguageAdapter(this, R.layout.auto_complete_line, it)
            editSelectLanguage.setAdapter(languageAdapter)
        })
        editSelectLanguage.threshold = 1
        editSelectLanguage.setOnItemClickListener { parent, _, position, _ ->
            val selectLanguage = parent.adapter.getItem(position) as Language?
            if (selectLanguage != null) {
                editSelectLanguage.showDropDown()
                editSelectLanguage.setText(selectLanguage.name)
                editSelectLanguage.setSelection(editSelectLanguage.text.length)
                editSelectLanguage.requestFocus()
                addFilterParametersWithPageZero(LANGUAGE_ID, selectLanguage.id)
            }
        }

        editSelectLanguage.doAfterTextChanged {
            if (it.isNullOrEmpty()) {
                changeFilterParameters(LANGUAGE_ID)
            }
        }

        additionalDataViewModel.speakers.observe(this, Observer {
            val speakersAdapter = SpeakersAdapter(this, R.layout.auto_complete_line, it)
            editSelectSpeaker.setAdapter(speakersAdapter)
        })
        editSelectSpeaker.threshold = 1
        editSelectSpeaker.setOnItemClickListener { parent, _, position, _ ->
            val selectSpeaker = parent.adapter.getItem(position) as Speakers?
            editSelectSpeaker.setText(getString(R.string.speakers_name_and_surname, selectSpeaker?.firstName, selectSpeaker?.lastName))
            editSelectSpeaker.setSelection(editSelectSpeaker.text.length)
            addFilterParametersWithPageZero(SPEAKERS_ID, "*")
        }

        editSelectSpeaker.doAfterTextChanged {
            if (it.isNullOrEmpty()) {
                changeFilterParameters(SPEAKERS_ID)
            }
        }

        val motionLayout = findViewById<MotionLayout>(R.id.sliding_container)
        motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) { /* stub */ }

            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) { /* stub */ }

            override fun onTransitionChange(motionLayout: MotionLayout, startId: Int, endId: Int, progress: Float) {
                dot_view_pager.layoutParams.height = (this@MainActivity.resources.getDimension(
                    R.dimen.pager_height) * (1 - progress)).roundToInt()
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) { /* stub */ }
        })

        checkBox.setOnCheckedChangeListener { _, isChecked ->
            val format = if (isChecked) {
                "ONLINE"
            } else {
                ""
            }
            addFilterParametersWithPageZero(FORMAT, format)
        }
    }

    private fun changeFilterParameters(parameter: String) {
        eventsViewModel.queryMap[PAGE] = 0
        eventsViewModel.queryMap.remove(parameter)
        eventsViewModel.isRefreshed.postValue(true)
        eventsViewModel.getEvents()
    }

    private fun addFilterParametersWithPageZero(parameter: String, value: Any) {
        eventsViewModel.queryMap[parameter] = value
        eventsViewModel.queryMap[PAGE] = 0
        eventsViewModel.isRefreshed.postValue(true)
        eventsViewModel.getEvents()
    }

    private fun setupHorizontalCalendarRecyclerView() {
        button_calendar.text = getMonthName(calendar.time)
        addDatesToList()

        horizontalCalendarRecyclerView = findViewById(R.id.recyclerview_date)

        horizontalCalendarLayoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        horizontalCalendarRecyclerView.layoutManager = horizontalCalendarLayoutManager

        horizontalCalendarAdapter = SimpleCalendarRecyclerViewAdapter(listOfDays, this)
        horizontalCalendarRecyclerView.adapter = horizontalCalendarAdapter

        horizontalCalendarRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dx > 0 && !horizontalCalendarRecyclerView.canScrollHorizontally(LayoutDirection.RTL)) {
                    addDatesToList()
                    horizontalCalendarAdapter.notifyDataSetChanged()
                }

                val temp = horizontalCalendarLayoutManager.findFirstVisibleItemPosition() + shift
                button_calendar.text = getMonthName(listOfDays[temp])

            }
        })

        button_calendar.setOnClickListener {
            horizontalCalendarAdapter.setNormalBackground()
        }
    }

    private fun addDatesToList() {
        val daysInMonth = calendar.getActualMaximum(DAY_OF_MONTH)

        for (i in calendar.get(DAY_OF_MONTH)..daysInMonth) {
            listOfDays.add(calendar.time)
            calendar.add(DAY_OF_MONTH, 1)
        }

        if (listOfDays.size < minHorizontalCalendarItemsCount) { addDatesToList() }
    }

    private fun getMonthName(date: Date): String {
        return monthFormat.format(date)
    }

    private fun setupRecyclerViewAndAdapter() {
        eventsRecycler = binding.eventsRecycler
        eventsRecycler.setHasFixedSize(true)

        // init vertical Layout Manager
        eventsLayoutManager = LinearLayoutManager(this)

        eventsRecycler.layoutManager = eventsLayoutManager

        eventsRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val tempValue = eventsLayoutManager.findFirstCompletelyVisibleItemPosition()
                if (eventsRecyclerCompletelyVisibleItemPosition != tempValue) {
                    eventsRecyclerCompletelyVisibleItemPosition = tempValue
                    try {
                        horizontalCalendarAdapter.setActiveItem(eventRecyclerViewAdapter.getData()[tempValue].startDateTime, dy < 0)
                    } catch (ex: Exception) { /* stub */ }
                }
            }
        })

        eventsRecycler.addOnScrollListener(object : PaginationScrollListener(eventsLayoutManager) {
            override fun isLastPage(): Boolean {
                return eventsPage.last
            }

            override fun subscribeToResults() {
                eventsViewModel.isEventsLoaded.observe(this@MainActivity, Observer {
                    isLoaded = it
                })
                eventsViewModel.isRefreshed.observe(this@MainActivity, Observer {
                    totalItemCount = 0
                    lastVisibleItem = 0
                    previousTotal = 0
                })
            }

            override fun loadMoreItems() {
                binding.eventLoadingProgressbar.visibility = View.VISIBLE
                eventsViewModel.queryMap[PAGE] = eventsPage.number.inc()
                eventsViewModel.getEvents()
            }
        })

        eventRecyclerViewAdapter = EventRecyclerViewAdapter(this)
        eventsRecycler.adapter = eventRecyclerViewAdapter
    }

    private fun onDotPageSelected(position: Int) {
        when (position) {
            0 -> {
                image_dot_one.setImageResource(R.drawable.ic_dot_active)
                image_dot_two.setImageResource(R.drawable.ic_dot)
                image_dot_three.setImageResource(R.drawable.ic_dot)
            }
            1 -> {
                image_dot_one.setImageResource(R.drawable.ic_dot)
                image_dot_two.setImageResource(R.drawable.ic_dot_active)
                image_dot_three.setImageResource(R.drawable.ic_dot)
            }
            2 -> {
                image_dot_one.setImageResource(R.drawable.ic_dot)
                image_dot_two.setImageResource(R.drawable.ic_dot)
                image_dot_three.setImageResource(R.drawable.ic_dot_active)
            }
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.mainToolBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        drawerToggle = ActionBarDrawerToggle(this, drawerLayout, binding.mainToolBar, R.string.open, R.string.close)
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

    /***
     * Requirements for draft event:
     * Title effective length must be from 10 to 80.
     * Value of startDateTime must be in future and before value finishDateTime.
     * Category id must be valid id from list of categories that was provided.
     * Language id must be valid id from list of languages that was provided.
     * If field format set to ONLINE(0) - field url is mandatory:
     * Url can’t be null or empty;
     * Address must be null or empty.
     * If field set to OFFLINE(1) - fields address and city:id are mandatory:
     * Address effective length must be from 10 to 80;
     * City id must be valid id from list of cities that was provided;
     * Url is not controlling. Can be either filled or not.
     */
    fun showNewEventActivity(view: View) {
        startActivity(Intent(this, NewEventActivity::class.java))
    }

    override fun performAction(itemId: Int, eventId: Int) {
        if (isOnline(this)) {
            when (itemId) {
                R.id.registration -> eventsViewModel.goingToAttend(eventId)
                R.id.cancel_registration -> eventsViewModel.doNotWantToAttend(eventId)
                R.id.become_a_speaker -> { /* stub */ }
                R.id.add_to_favorites -> { /* stub */ }
            }
        }
    }

    override fun showFullEventInfo(eventId: Int) {
        startActivity(Intent(this, FullEventInfoActivity::class.java).putExtra(EVENT, eventId))
    }

    companion object {
        private var eventsViewModel: EventsViewModel? = null

        fun obtainViewModel(activity: AppCompatActivity): EventsViewModel {
            if (eventsViewModel == null) {
                eventsViewModel = ViewModelProviders.of(activity).get(EventsViewModel::class.java)
            }
            return eventsViewModel as EventsViewModel
        }
    }

    fun onDrawerMenuItemClick(item: MenuItem) {
        when (item.itemId) {
            R.id.sign_out -> signOut()
            R.id.dashboard -> {
                drawerLayout.closeDrawer(GravityCompat.START)
            }
        }
    }

    //onClick for "sign out" menu item
    private fun signOut() {
        getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
            .edit()
            .remove(EMAIL)
            .remove(PASS_CODE)
            .remove(USER_TOKEN)
            .apply()

        AppApplication.token = ""

        startActivity(Intent(this, StartActivity::class.java))
        finish()
    }

    override fun onItemClick(position: Int) {
        when (val eventRecyclerPosition = eventRecyclerViewAdapter.getData()
                .indexOfFirst { event: Event ->  matchFormat.format(event.startDateTime) == matchFormat.format(listOfDays[position].time) }) {
            -1 -> {
                isCustomCalendarItemPositionFound = false
                customCalendarItemClickPreviousPosition = position
                eventsRecycler.scrollToPosition(eventsLayoutManager.itemCount)
                eventsRecycler.smoothScrollToPosition(eventsLayoutManager.itemCount + 1)
            }
            0 -> {
                isCustomCalendarItemPositionFound = true
                eventsRecycler.scrollToPosition(1)
                eventsRecycler.smoothScrollToPosition(0)
            }
            else -> {
                isCustomCalendarItemPositionFound = true
                eventsRecycler.smoothScrollToPosition(eventRecyclerPosition.plus(eventItemOffset))
            }
        }
    }
}