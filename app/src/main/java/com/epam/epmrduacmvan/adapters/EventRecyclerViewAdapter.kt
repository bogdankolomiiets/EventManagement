package com.epam.epmrduacmvan.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.MenuCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.epam.epmrduacmvan.AppApplication
import com.epam.epmrduacmvan.Constants.Companion.ROUND_ICONS_HEIGHT
import com.epam.epmrduacmvan.Constants.Companion.ROUND_ICONS_WIDTH
import com.epam.epmrduacmvan.R
import com.epam.epmrduacmvan.UrlConstants.Companion.BASE_URL
import com.epam.epmrduacmvan.UrlConstants.Companion.ROUND_IMAGE_CONTROLLER
import com.epam.epmrduacmvan.databinding.SingleEventItemBinding
import com.epam.epmrduacmvan.model.Event
import com.epam.epmrduacmvan.model.Event.Companion.ATTENDEE
import com.epam.epmrduacmvan.model.Event.Companion.ORGANIZER
import com.epam.epmrduacmvan.model.Event.Companion.SPEAKER
import com.epam.epmrduacmvan.utils.EventItemClickListener
import com.epam.epmrduacmvan.utils.EventPopupMenuItemClickListener
import kotlinx.android.synthetic.main.single_event_item.view.*
import java.lang.Exception


class EventRecyclerViewAdapter(private val eventPopupMenuItemClickListener: EventPopupMenuItemClickListener) :
    RecyclerView.Adapter<EventRecyclerViewAdapter.EventViewHolder>() {
    private lateinit var binding: SingleEventItemBinding
    private val events = mutableListOf<Event>()
    // Create layout parameters for ImageView
    private val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.single_event_item, parent, false)
        return EventViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return events.size
    }

    override fun onViewRecycled(holder: EventViewHolder) {
        super.onViewRecycled(holder)
        holder.itemView.speakers_container.removeAllViews()
        holder.itemView.speaker.visibility = View.INVISIBLE
        holder.itemView.participant.visibility = View.INVISIBLE
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.bind(event)

        //creating icon for speakers
        if (!event.speakers.isNullOrEmpty()) {
            event.speakers.forEach {
                if (it.photoUrl.isNotEmpty()) {
                    val speakerIcon = ImageView(holder.itemView.context)
                    speakerIcon.setPadding(8, 4, 8, 8)
                    speakerIcon.layoutParams = layoutParams
                    Glide.with(AppApplication.appContext)
                            .load(BASE_URL.plus(ROUND_IMAGE_CONTROLLER).plus(it.photoUrl))
                            .error(R.drawable.account)
                            .override(ROUND_ICONS_WIDTH, ROUND_ICONS_HEIGHT)
                            .into(speakerIcon)
                    holder.itemView.speakers_container.addView(speakerIcon)
                }
            }
        }

        //creating a popup menu
        val popupMenu = PopupMenu(holder.itemView.context, holder.itemView.overflow_menu)
        //inflating menu from xml resource
        popupMenu.inflate(R.menu.event_popup_menu)

        when (event.attendeeType) {
            SPEAKER -> {
                holder.itemView.speaker.visibility = View.VISIBLE
                configurePopup(popupMenu)
            }
            ATTENDEE -> {
                holder.itemView.participant.visibility = View.VISIBLE
                configurePopup(popupMenu)
            }
            ORGANIZER -> {
                holder.itemView.speaker.visibility = View.INVISIBLE
                holder.itemView.participant.visibility = View.INVISIBLE
                popupMenu.menu.findItem(R.id.registration).isVisible = false
                popupMenu.menu.findItem(R.id.cancel_registration).isVisible = false
            }
            else -> {
                holder.itemView.speaker.visibility = View.INVISIBLE
                holder.itemView.participant.visibility = View.INVISIBLE
                popupMenu.menu.findItem(R.id.registration).isVisible = true
                popupMenu.menu.findItem(R.id.cancel_registration).isVisible = false
            }
        }

        //set horizontal divider for groups
        MenuCompat.setGroupDividerEnabled(popupMenu.menu, true)

        popupMenu.setOnMenuItemClickListener {
            eventPopupMenuItemClickListener.performAction(it.itemId, events[holder.adapterPosition].id)
            return@setOnMenuItemClickListener true
        }

        /*
        *useful code for showing icons
        *from https://resocoder.com/2018/02/02/popup-menu-with-icons-android-kotlin-tutorial-code/
        */
        try {
            val fieldMPopup = PopupMenu::class.java.getDeclaredField("mPopup")
            fieldMPopup.isAccessible = true
            val mPopup = fieldMPopup[popupMenu]
            mPopup.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(mPopup, true)
        } catch (ex: Exception) { /* stub */
        }

        holder.itemView.overflow_menu.setOnClickListener {
            popupMenu.show()
        }
        holder.itemView.setOnClickListener {
            (eventPopupMenuItemClickListener as EventItemClickListener).showFullEventInfo(events[holder.adapterPosition].id)
        }
    }

    private fun configurePopup(popupMenu: PopupMenu) {
        popupMenu.menu.findItem(R.id.registration).isVisible = false
        popupMenu.menu.findItem(R.id.cancel_registration).isVisible = true
    }


    fun addData(listEvents: List<Event>, isRefreshed: Boolean) {
        if (!isRefreshed) {
            val startPosition = events.size
            events.addAll(listEvents)
            notifyItemRangeInserted(startPosition.inc(), events.size)
        } else {
            events.clear()
            events.addAll(listEvents)

            notifyDataSetChanged()
        }
    }

    fun refreshEvent(updatedEvent: Event) {
        (0..events.size).forEach { i ->
            if (events[i].id == updatedEvent.id) {
                events[i] = updatedEvent
                notifyItemChanged(i)
                return
            }
        }
    }

    fun getData(): List<Event> {
        return events
    }

    inner class EventViewHolder(private val binding: SingleEventItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            binding.event = event
        }
    }
}
