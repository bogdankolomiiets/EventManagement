package com.epam.epmrduacmvan.views

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.epam.epmrduacmvan.Constants.Companion.CITY
import com.epam.epmrduacmvan.R
import com.epam.epmrduacmvan.adapters.CitySearchRecyclerViewAdapter
import com.epam.epmrduacmvan.model.City
import com.epam.epmrduacmvan.viewmodels.AdditionalDataViewModel

class CitySearchActivity: AppCompatActivity() {
    private lateinit var searchToolbar: androidx.appcompat.widget.Toolbar
    private lateinit var cityName: TextView
    private lateinit var citySearchRecyclerViewAdapter: CitySearchRecyclerViewAdapter
    private var filteredCities: MutableList<City> = mutableListOf()
    private lateinit var additionalDataViewModel: AdditionalDataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_search)
        searchToolbar = findViewById(R.id.search_toolbar)
        setSupportActionBar(searchToolbar)

        additionalDataViewModel = ViewModelProviders.of(this).get(AdditionalDataViewModel::class.java)
        additionalDataViewModel.getCities().observe(this, Observer {
            filteredCities.clear()
            filteredCities.addAll(it)
            citySearchRecyclerViewAdapter.setData(filteredCities)
        })

        cityName = findViewById(R.id.city_name)
        cityName.doAfterTextChanged { text -> additionalDataViewModel.cityName.postValue(text.toString())}


        val cityRecyclerView = findViewById<RecyclerView>(R.id.city_recycler_view)
        cityRecyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        cityRecyclerView.layoutManager = layoutManager
        citySearchRecyclerViewAdapter = CitySearchRecyclerViewAdapter()
        citySearchRecyclerViewAdapter.setOnClickListener(View.OnClickListener { view ->
            setResult(RESULT_OK, Intent(this@CitySearchActivity, MainActivity::class.java)
                .putExtra(CITY, filteredCities[cityRecyclerView.getChildAdapterPosition(view)]))
            finish()
        })

        cityRecyclerView.adapter = citySearchRecyclerViewAdapter
        val decoration = DividerItemDecoration(this, layoutManager.orientation)
        cityRecyclerView.addItemDecoration(decoration)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.clear_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.clear_search -> {
                if (cityName.text.isNotEmpty()) {
                    cityName.text = ""
                } else {
                    onBackPressed()
                }
            }
        }
        return true
    }
}