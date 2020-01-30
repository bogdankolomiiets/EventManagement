package com.epam.epmrduacmvan.views

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.epam.epmrduacmvan.Constants
import com.epam.epmrduacmvan.Constants.Companion.EVENT_ID
import com.epam.epmrduacmvan.R
import com.epam.epmrduacmvan.RequestResponseCodes.Companion.YOUTUBE_LINK_ADDED_OK
import com.epam.epmrduacmvan.RequestResponseCodes.Companion.YOUTUBE_LINK_SERVER_CONTAINS
import com.epam.epmrduacmvan.model.Artifact
import com.epam.epmrduacmvan.model.YoutubeVideo
import com.epam.epmrduacmvan.utils.showErrorToast
import com.epam.epmrduacmvan.viewmodels.AdditionalDataViewModel
import java.util.regex.Pattern

class ProvideYoutubeLinkActivity: AppCompatActivity() {
    private lateinit var additionalDataViewModel: AdditionalDataViewModel
    private lateinit var youtubeLink: EditText
    private lateinit var youtubeThumbnail: ImageView
    private lateinit var saveLinkButton: Button
    private val youtubeVideo: MutableList<YoutubeVideo> = mutableListOf()
    private val pattern = Pattern.compile(Constants.YOUTUBE_LINK_PATTERN)
    private val thumbnailQuality = "standard"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_provide_youtube_link)
        setSupportActionBar(findViewById(R.id.youtube_link_tool_bar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        additionalDataViewModel = ViewModelProviders.of(this)[AdditionalDataViewModel::class.java]
        val eventId = intent.getIntExtra(EVENT_ID, -1)

        //init UI components
        youtubeLink = findViewById(R.id.youtube_link)
        youtubeThumbnail = findViewById(R.id.youtube_thumbnail)
        saveLinkButton = findViewById(R.id.save_link_button)
        saveLinkButton.setOnClickListener {
            if (youtubeVideo.isNotEmpty()) {
                additionalDataViewModel.addLinkToEvent(eventId.toString(), Artifact(youtubeLink.text.toString(), youtubeVideo[0].snippet.title, Artifact.TYPE_LINK))
            } else showErrorToast(getString(R.string.please_provide_youtube_link))
        }

        additionalDataViewModel.getYoutubeLinkAddingResult().observe(this, Observer {
            when (it) {
                YOUTUBE_LINK_ADDED_OK -> {
                    showErrorToast(getString(R.string.link_added))
                    finish()
                }
                YOUTUBE_LINK_SERVER_CONTAINS -> showErrorToast(getString(R.string.already_exists))
            }
        })

        val linkBuilder = StringBuilder()
        youtubeLink.doOnTextChanged { text, _, _, _ ->
            try {
                val matcher = pattern.matcher(text)
                if (matcher.find()) {
                    linkBuilder.clear()
                    linkBuilder.append(matcher.group(1))

                    if (linkBuilder.isNotEmpty()) {
                        additionalDataViewModel.getVideoInfo(linkBuilder.toString())
                    }
                }
            } catch (ex: Exception) {
                /* stub */
            }
        }

        additionalDataViewModel.getYoutubeVideos().observe(this, Observer {
            if (it.items.isNotEmpty()) {
                youtubeVideo.clear()
                youtubeVideo.addAll(it.items)

                Glide.with(this)
                    .load(youtubeVideo[0].snippet.thumbnails[thumbnailQuality]?.url)
                    .into(youtubeThumbnail)
            } else {
                youtubeThumbnail.setImageResource(0)
                youtubeVideo.clear()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.clear_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.clear_search, android.R.id.home -> onBackPressed()
        }
        return true
    }
}