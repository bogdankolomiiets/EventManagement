package com.epam.epmrduacmvan.views

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.epam.epmrduacmvan.Constants
import com.epam.epmrduacmvan.R
import com.epam.epmrduacmvan.utils.showSplashScreen
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        showSplashScreen(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        if (haveUserData()) {
            val navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            val navGraph = navController.getNavInflater().inflate(R.navigation.main_nav_graph);
            navGraph.startDestination = R.id.authorisationEnterPassCodeFragment
            navController.graph = navGraph
        }

        setSupportActionBar(authorisation_tool_bar)
    }

    private fun haveUserData(): Boolean {
        sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        return sharedPreferences.contains(Constants.EMAIL) and sharedPreferences.contains(Constants.PASSCODE)
    }
}