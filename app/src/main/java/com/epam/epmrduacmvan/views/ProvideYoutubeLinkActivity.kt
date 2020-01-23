package com.epam.epmrduacmvan.views

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.epam.epmrduacmvan.Constants.Companion.EVENT
import com.epam.epmrduacmvan.R
import com.epam.epmrduacmvan.model.Artifact
import com.epam.epmrduacmvan.viewmodels.AdditionalDataViewModel
import java.lang.StringBuilder
import java.util.function.Predicate

class ProvideYoutubeLinkActivity: AppCompatActivity() {
    private lateinit var additionalDataViewModel: AdditionalDataViewModel
    private lateinit var youtubeLink: EditText
    private lateinit var youtubeThumbnail: ImageView
    private lateinit var saveLinkButton: Button
    private val imgYoutubeLink = "http://img.youtube.com/vi/"
    private val imageResolutionForYoutubeThumbnail = "/sddefault.jpg"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_provide_youtube_link)
        setSupportActionBar(findViewById(R.id.youtube_link_tool_bar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        additionalDataViewModel = ViewModelProviders.of(this)[AdditionalDataViewModel::class.java]
        val eventId = intent.getIntExtra(EVENT, -1)

        //init UI components
        youtubeLink = findViewById(R.id.youtube_link)
        youtubeThumbnail = findViewById(R.id.youtube_thumbnail)
        saveLinkButton = findViewById(R.id.save_link_button)
        saveLinkButton.setOnClickListener {
            val tempLink = youtubeLink.text.toString()
            if (tempLink.isNotEmpty() and (eventId > -1)) {
                additionalDataViewModel.addLinkToEvent(eventId.toString(), Artifact(tempLink, "New file for demo"))
            }
        }

        val linkBuilder = StringBuilder()
        youtubeLink.doOnTextChanged { text, _, _, _ ->
            try {
                linkBuilder.clear()
                linkBuilder.append(text.toString().split("?v=")[1])
                if (linkBuilder.isNotEmpty()) {
                    additionalDataViewModel.getVideoInfo(linkBuilder.toString())
                }
            } catch (ex: Exception) {
                /* stub */
            }
        }

        additionalDataViewModel.getYoutubeVideos().observe(this, Observer {
            if (it.items.isNotEmpty()) {
                Glide.with(this)
                    .load(it.items[0].snippet.thumbnails["default"])
                    .into(youtubeThumbnail)

                Toast.makeText(ProvideYoutubeLinkActivity@this, it.items[0].snippet.title, Toast.LENGTH_LONG).show()
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