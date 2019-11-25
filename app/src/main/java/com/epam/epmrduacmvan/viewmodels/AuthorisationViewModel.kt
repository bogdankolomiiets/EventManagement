package com.epam.epmrduacmvan.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class AuthorisationViewModel(application: Application) : AndroidViewModel(application) {

    var userEmail: String = ""
}