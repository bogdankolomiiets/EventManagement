package com.epam.epmrduacmvan.repository

object BackendUserAuthorisationServiceImpl : UserAuthorisationService {

    override fun sendCodeOnEmail(userEmail: String) {
        println("code sent on email $userEmail")
    }

    override fun confirmEmail(userEmail: String, codeFromEmail: Int) {
    }

    override fun setPassCode(passCode: Int) {
    }
}