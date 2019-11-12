package com.epam.epmrduacmvan.repository

interface UserAuthorisationService {
    fun sendCodeOnEmail(userEmail: String)
    fun confirmEmail(userEmail: String, codeFromEmail: Int)
    fun setPassCode(passCode: Int)
}