package com.epam.epmrduacmvan.views

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.epam.epmrduacmvan.Constants.Companion.EMAIL
import com.epam.epmrduacmvan.Constants.Companion.NO_INFORMATION
import com.epam.epmrduacmvan.Constants.Companion.PASS_CODE
import com.epam.epmrduacmvan.Constants.Companion.WITHOUT_PASSCODE
import com.epam.epmrduacmvan.Constants.Companion.WITH_PASSCODE
import com.epam.epmrduacmvan.AppApplication
import com.epam.epmrduacmvan.Constants
import com.epam.epmrduacmvan.Constants.Companion.EMPTY_PASSCODE
import com.epam.epmrduacmvan.R
import com.epam.epmrduacmvan.RequestResponseCodes
import com.epam.epmrduacmvan.RequestResponseCodes.Companion.BAD_REQUEST_400
import com.epam.epmrduacmvan.RequestResponseCodes.Companion.INTERNAL_SERVER_ERROR_500
import com.epam.epmrduacmvan.RequestResponseCodes.Companion.NOT_FOUND_404
import com.epam.epmrduacmvan.RequestResponseCodes.Companion.PASSCODE_REMOVED
import com.epam.epmrduacmvan.RequestResponseCodes.Companion.PASSCODE_SET_EMPTY
import com.epam.epmrduacmvan.RequestResponseCodes.Companion.PASSCODE_SET_NOT_EMPTY
import com.epam.epmrduacmvan.RequestResponseCodes.Companion.RESPONSE_BODY_TO_JSON_FAIL
import com.epam.epmrduacmvan.utils.showCustomSnack
import com.epam.epmrduacmvan.utils.showSplashScreen
import kotlinx.android.synthetic.main.custom_tool_bar.*
import com.epam.epmrduacmvan.viewmodels.AuthorisationViewModel

class StartActivity : AppCompatActivity() {
    private val sharedPreferences = AppApplication.sharedPreferences
    private lateinit var rootContainer: View

    override fun onCreate(savedInstanceState: Bundle?) {
        showSplashScreen(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        rootContainer = findViewById(R.id.start_layout_container)

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

        custom_authorisation_toolbar_up_button.setOnClickListener { onBackPressed() }

        obtainViewModel(supportFragmentManager.fragments.first()).passCodeRequestStatus.observe(this, Observer {
            when (it){
                PASSCODE_SET_EMPTY, PASSCODE_SET_NOT_EMPTY -> {
                    navController.navigate(R.id.mainActivity)
                    finish()
                }
                PASSCODE_REMOVED -> showCustomSnack(rootContainer, R.string.passcode_removed)
                BAD_REQUEST_400 -> showCustomSnack(rootContainer, R.string.bad_request)
                NOT_FOUND_404 -> showCustomSnack(rootContainer, R.string.not_found)
                INTERNAL_SERVER_ERROR_500 -> showCustomSnack(rootContainer, R.string.internal_server_error)
                RESPONSE_BODY_TO_JSON_FAIL -> showCustomSnack(rootContainer, R.string.bad_response)
            }
        })
    }

    private fun haveUserData(): Int {
        return if (!sharedPreferences.contains(EMAIL)) {
            NO_INFORMATION
        } else if (sharedPreferences.getString(PASS_CODE, "") == EMPTY_PASSCODE) {
            WITHOUT_PASSCODE
        } else WITH_PASSCODE
    }

    companion object {
        private var authorisationViewModel: AuthorisationViewModel? = null

        fun obtainViewModel(fragment: Fragment): AuthorisationViewModel {
            if (authorisationViewModel == null){
                authorisationViewModel = ViewModelProviders.of(fragment).get(AuthorisationViewModel::class.java)
            }
            return authorisationViewModel as AuthorisationViewModel
        }
    }
}