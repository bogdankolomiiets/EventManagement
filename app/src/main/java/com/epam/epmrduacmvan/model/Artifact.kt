package com.epam.epmrduacmvan.model

data class Artifact(val id: Int = 0,
                    val artifactUrl: String = "",
                    val fileName: String = "",
                    val fileSize: Int = 0,
                    val artifactType: String = "") {

    constructor(url: String, name: String, type: String) : this(artifactUrl = url, fileName = name, artifactType = type)

    companion object {
        const val TYPE_LINK = "LINK"
        const val TYPE_IMAGE = "IMAGE"
        const val TYPE_VIDEO = "VIDEO"
        const val TYPE_DOCUMENT = "DOCUMENT"
    }
}