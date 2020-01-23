package com.epam.epmrduacmvan.model

data class Artifact(val id: Int = 0,
                    val artifactUrl: String = "",
                    val fileName: String = "",
                    val fileSize: Int = 0,
                    val link: Boolean = false) {

    constructor(url: String, name: String) : this(artifactUrl = url, fileName = name, link = true)
}