package com.epam.epmrduacmvan.repository

class RepositoryProvider private constructor(){

    companion object Instance {
        val eventRepository: EventRepository by lazy { BackendEventRepositoryImpl }
        val passcodeRepository: PassCodeRepository by lazy { BackendPassCodeRepositoryImpl }
        val authorisationRepository: AuthorisationRepository by lazy { BackendAuthorisationRepositoryImpl }
    }
}