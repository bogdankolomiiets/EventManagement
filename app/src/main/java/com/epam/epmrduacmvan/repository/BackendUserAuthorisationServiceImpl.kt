package com.epam.epmrduacmvan.repository

object BackendUserAuthorisationServiceImpl : UserAuthorisationService {

    override fun sendCodeOnEmail(userEmail: String) {
        println("code sent on email $userEmail")
    }

    override fun confirmEmail(userEmail: String, codeFromEmail: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setPassCode(passCode: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}