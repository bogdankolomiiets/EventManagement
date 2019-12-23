package com.epam.epmrduacmvan.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.epam.epmrduacmvan.R
import com.epam.epmrduacmvan.model.CalendarDays
import com.epam.epmrduacmvan.utils.CustomCalendarItemClickListener
import java.text.SimpleDateFormat
import java.util.*

class SimpleCalendarRecyclerViewAdapter(private val dateList: MutableList<Date>, private val customCalendarItemClickListener: CustomCalendarItemClickListener):
    RecyclerView.Adapter<SimpleCalendarRecyclerViewAdapter.SimpleCalendarViewHolder>() {
    private var previousHolder: SimpleCalendarViewHolder? = null
    private lateinit var recyclerView: RecyclerView
    private val dayNumberFormat = SimpleDateFormat("d", Locale.getDefault())
    private val matchFormat = SimpleDateFormat("dd.MM", Locale.getDefault())
    private val dayNameFormat = SimpleDateFormat("EEE", Locale.getDefault())
    private val shift = 1
    private var listCalendarCount: MutableList<CalendarDays> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleCalendarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_simple_calendar, parent, false)
        return SimpleCalendarViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dateList.size
    }

    override fun onViewRecycled(holder: SimpleCalendarViewHolder) {
        super.onViewRecycled(holder)
        if (holder == previousHolder) {
            setNormalBackground()
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onBindViewHolder(holder: SimpleCalendarViewHolder, position: Int) {
        holder.dayNumber.text = getDayNumber(dateList[position])
        holder.day.text = getDayName(dateList[position])

         if (listCalendarCount.isNotEmpty() && position < listCalendarCount.size) {
            holder.numberOfAllEvents.text = listCalendarCount[position].allCount.toString()
            holder.numberOfFilteredEvents.text = listCalendarCount[position].filteredCount.toString()
        } else if (position > listCalendarCount.size) {
            holder.numberOfAllEvents.text = "0"
            holder.numberOfFilteredEvents.text = "0"
        }

        holder.customCalendarLayout.setOnClickListener {
            customCalendarItemClickListener.onItemClick(position)
            setNormalBackground()

            previousHolder = holder

            holder.itemView.isActivated = true
            holder.lineBottom.visibility = View.VISIBLE
        }
    }

    fun setNormalBackground() {
        previousHolder?.itemView?.isActivated = false
        previousHolder?.lineBottom?.visibility = View.INVISIBLE
    }

    private fun getDayNumber(date: Date): String {
        return dayNumberFormat.format(date)
    }

    private fun getDayName(date: Date): String {
        return dayNameFormat.format(date)
    }

    fun setActiveItem(startDateTime: Long, isReverse: Boolean) {
        (0 .. dateList.size).forEach { i ->
            if (matchFormat.format(dateList[i].time) == matchFormat.format(startDateTime)) {
                setNormalBackground()
                previousHolder = recyclerView.findViewHolderForLayoutPosition(i) as SimpleCalendarViewHolder
                previousHolder?.itemView?.isActivated = true
                previousHolder?.lineBottom?.visibility = View.VISIBLE
                recyclerView.scrollToPosition(previousHolder?.adapterPosition ?: 0)
                recyclerView.smoothScrollToPosition(previousHolder?.adapterPosition?.plus(if (isReverse) -shift else shift) ?: 0)
                return
            }
        }
    }

    fun updateCalendarData(listCountOfDays: List<CalendarDays>) {
        listCalendarCount.clear()
        listCalendarCount.addAll(listCountOfDays)
        notifyDataSetChanged()
    }

    inner class SimpleCalendarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayNumber: TextView = itemView.findViewById(R.id.text_day_number)
        val day: TextView = itemView.findViewById(R.id.text_day)
        val numberOfFilteredEvents: TextView = itemView.findViewById(R.id.text_number_of_filtered_events)
        val numberOfAllEvents: TextView = itemView.findViewById(R.id.text_number_of_all_events)
        val lineBottom: TextView = itemView.findViewById(R.id.text_line_bottom)
        val customCalendarLayout: ConstraintLayout = itemView.findViewById(R.id.custom_tab_item)
    }
}