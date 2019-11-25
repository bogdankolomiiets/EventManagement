package com.epam.epmrduacmvan.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import com.epam.epmrduacmvan.Constants.Companion.EMAIL
import com.epam.epmrduacmvan.Constants.Companion.PASS_CODE
import com.epam.epmrduacmvan.Constants.Companion.SHARED_PREF
import com.epam.epmrduacmvan.R
import com.epam.epmrduacmvan.databinding.ActivityMainBinding
import com.epam.epmrduacmvan.utils.showCustomToastNoInternet
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity: AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        drawerLayout = binding.drawerLayout
        drawer_navigation_view.setNavigationItemSelectedListener(this)
        setupActionBar()
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.mainToolBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        drawerToggle = ActionBarDrawerToggle(this, drawerLayout, binding.mainToolBar,
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
        } else{
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
    fun signOut(item: MenuItem){
        getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
            .edit()
            .remove(EMAIL)
            .remove(PASS_CODE)
            .apply()

        startActivity(Intent(this, StartActivity::class.java))
        finish()
    }

    fun search(item: MenuItem) {
        showCustomToastNoInternet(binding.mainToolBar)
    }
    fun filter(item: MenuItem) {}
}