package com.epam.epmrduacmvan.model

data class YoutubeVideoSnippet(val title: String,
                               val description: String,
                               val thumbnails: Map<String, YoutubeVideoThumbnail>,
                               val channelTitle: String)