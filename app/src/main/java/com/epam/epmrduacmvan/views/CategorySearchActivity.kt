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
import com.epam.epmrduacmvan.Constants.Companion.CATEGORY
import com.epam.epmrduacmvan.R
import com.epam.epmrduacmvan.adapters.CategorySearchRecyclerViewAdapter
import com.epam.epmrduacmvan.model.Category
import com.epam.epmrduacmvan.viewmodels.AdditionalDataViewModel

class CategorySearchActivity: AppCompatActivity() {
    private lateinit var searchToolbar: androidx.appcompat.widget.Toolbar
    private lateinit var categoryName: TextView
    private lateinit var categorySearchRecyclerViewAdapter: CategorySearchRecyclerViewAdapter
    private var filteredCategories: MutableList<Category> = mutableListOf()
    private lateinit var additionalDataViewModel: AdditionalDataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_search)
        searchToolbar = findViewById(R.id.search_toolbar)
        setSupportActionBar(searchToolbar)

        additionalDataViewModel = ViewModelProviders.of(this).get(AdditionalDataViewModel::class.java)
        additionalDataViewModel.getCategories().observe(this, Observer {
            filteredCategories.clear()
            filteredCategories.addAll(it)
            categorySearchRecyclerViewAdapter.setData(filteredCategories)
        })

        categoryName = findViewById(R.id.category_name)
        categoryName.doAfterTextChanged { text -> additionalDataViewModel.categoryName.postValue(text.toString())}


        val categoryRecyclerView = findViewById<RecyclerView>(R.id.category_recycler_view)
        categoryRecyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        categoryRecyclerView.layoutManager = layoutManager
        categorySearchRecyclerViewAdapter = CategorySearchRecyclerViewAdapter()
        categorySearchRecyclerViewAdapter.setOnClickListener(View.OnClickListener { view ->
            setResult(RESULT_OK, Intent(this@CategorySearchActivity, MainActivity::class.java)
                .putExtra(CATEGORY, filteredCategories[categoryRecyclerView.getChildAdapterPosition(view)]))
            finish()
        })

        categoryRecyclerView.adapter = categorySearchRecyclerViewAdapter
        val decoration = DividerItemDecoration(this, layoutManager.orientation)
        categoryRecyclerView.addItemDecoration(decoration)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.clear_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.clear_search -> {
                if (categoryName.text.isNotEmpty()) {
                    categoryName.text = ""
                } else {
                    onBackPressed()
                }
            }
        }
        return true
    }
}