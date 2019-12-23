package com.epam.epmrduacmvan.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.epam.epmrduacmvan.AppApplication
import com.epam.epmrduacmvan.Constants.Companion.EVENT
import com.epam.epmrduacmvan.Constants.Companion.ROUND_ICONS_HEIGHT
import com.epam.epmrduacmvan.Constants.Companion.ROUND_ICONS_WIDTH
import com.epam.epmrduacmvan.R
import com.epam.epmrduacmvan.UrlConstants.Companion.BASE_URL
import com.epam.epmrduacmvan.UrlConstants.Companion.ROUND_IMAGE_CONTROLLER
import com.epam.epmrduacmvan.databinding.FragmentFeaturedEventBinding
import com.epam.epmrduacmvan.model.Event

class FeaturedEventFragment : Fragment() {
    private lateinit var binding: FragmentFeaturedEventBinding
    private lateinit var event: Event
    // Create layout parameters for ImageView
    private val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_featured_event, container, false)

        binding.root.setOnClickListener {
            startActivity(Intent(this.context, FullEventInfoActivity::class.java).putExtra(EVENT, event.id))
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.event = event

        //creating icon for speakers
        if (!event.speakers.isNullOrEmpty()) {
            event.speakers.forEach {
                if (it.photoUrl.isNotEmpty()) {
                    val speakerIcon = ImageView(binding.root.context)
                    speakerIcon.setPadding(0, 4, 0, 4)
                    speakerIcon.layoutParams = layoutParams
                    Glide.with(AppApplication.appContext)
                            .load(BASE_URL.plus(ROUND_IMAGE_CONTROLLER).plus(it.photoUrl))
                            .error(R.drawable.account)
                            .override(ROUND_ICONS_WIDTH, ROUND_ICONS_HEIGHT)
                            .into(speakerIcon)
                    binding.speakersContainer.addView(speakerIcon)
                }
            }
        }
    }

    fun bindEvent(event: Event){
        this.event = event
    }
}
