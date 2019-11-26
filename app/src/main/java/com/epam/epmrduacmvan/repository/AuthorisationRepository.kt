package com.epam.epmrduacmvan.repository

import androidx.lifecycle.MutableLiveData

interface AuthorisationRepository {
    fun sendCodeOnEmail(userEmail: String, codeRequestStatus: MutableLiveData<Int>)
    fun confirmEmail(userEmail: String, codeFromEmail: String, codeRequestStatus: MutableLiveData<Int>, tokenFromRequest: MutableLiveData<String>)
}