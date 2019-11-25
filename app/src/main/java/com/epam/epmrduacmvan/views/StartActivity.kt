package com.epam.epmrduacmvan.views

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.epam.epmrduacmvan.Constants.Companion.EMAIL
import com.epam.epmrduacmvan.Constants.Companion.NO_INFORMATION
import com.epam.epmrduacmvan.Constants.Companion.PASS_CODE
import com.epam.epmrduacmvan.Constants.Companion.SHARED_PREF
import com.epam.epmrduacmvan.Constants.Companion.WITHOUT_PASSCODE
import com.epam.epmrduacmvan.Constants.Companion.WITH_PASSCODE
import com.epam.epmrduacmvan.R
import com.epam.epmrduacmvan.utils.showSplashScreen
import kotlinx.android.synthetic.main.custom_tool_bar.*

class StartActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        showSplashScreen(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        val navGraph = navController.navInflater.inflate(R.navigation.main_nav_graph)
        navGraph.startDestination = (
                when (haveUserData()) {
                    WITHOUT_PASSCODE -> R.id.mainActivity
                    WITH_PASSCODE -> R.id.authorisationEnterPassCodeFragment
                    else -> R.id.authorisationEmailFragment
                })
        navController.graph = navGraph

        if (navGraph.startDestination == R.id.mainActivity) {
            finish()
        }

        custom_authorisation_tool_bar_up_button.setOnClickListener { onBackPressed() }
    }

    private fun haveUserData(): Int {
        sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE)
        return if (!sharedPreferences.contains(EMAIL)){
            NO_INFORMATION
        } else if (!sharedPreferences.contains(PASS_CODE)) {
            WITHOUT_PASSCODE
        } else WITH_PASSCODE
    }
}