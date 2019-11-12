package com.epam.epmrduacmvan.model

data class User (
    var id: Int = 0,
    var firstName: String = "",
    var lastName: String? = "",
    var password: String = "",
    var email: String = ""){
}