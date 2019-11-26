package com.epam.epmrduacmvan.repository

import androidx.lifecycle.MutableLiveData

interface PassCodeRepository {
    fun getPassCode(passCodeRequestStatus: MutableLiveData<Int>, passCodeFromRequest: MutableLiveData<String>)
    fun setPassCode(passCode: String, passCodeRequestStatus: MutableLiveData<Int>)
    fun removePassCode(passCode: String, passCodeRequestStatus: MutableLiveData<Int>)
}