package com.epam.epmrduacmvan.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.epam.epmrduacmvan.R
import com.epam.epmrduacmvan.databinding.SingleEventItemBinding
import com.epam.epmrduacmvan.model.Event

class EventRecyclerViewAdapter: RecyclerView.Adapter<EventRecyclerViewAdapter.EventViewHolder>() {
    private val events = mutableListOf<Event>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding: SingleEventItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.single_event_item, parent, false)
        return EventViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return events.size
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(events[position])

        /*if (position % 2 == 0) {
            holder.itemView.setBackgroundResource(android.R.color.white)
        } else holder.itemView.setBackgroundResource(R.color.colorAlabaster)*/
    }

    fun addData(listEvents: List<Event>) {
        val startPosition= events.size
        events.addAll(listEvents)
        notifyItemRangeInserted(startPosition.inc(), events.size)
    }

    inner class EventViewHolder(private val binding: SingleEventItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event){
            binding.event = event
        }
    }
}
