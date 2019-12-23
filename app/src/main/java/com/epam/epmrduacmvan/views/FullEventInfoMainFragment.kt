package com.epam.epmrduacmvan.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.epam.epmrduacmvan.R
import com.epam.epmrduacmvan.databinding.FragmentFullEventInfoMainBinding
import com.epam.epmrduacmvan.model.Event
import com.epam.epmrduacmvan.viewmodels.EventsViewModel


class FullEventInfoMainFragment : Fragment(){
    private lateinit var binding: FragmentFullEventInfoMainBinding
    private lateinit var eventsViewModel: EventsViewModel
    private lateinit var eventForDisplay: Event
    private var isEllipsized = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_full_event_info_main, container, false)

        eventsViewModel = MainActivity.obtainViewModel(activity as FullEventInfoActivity)
        eventsViewModel.eventById.observe(this, Observer {
            binding.event = it
            eventForDisplay = it
        })

        binding.readMore.setOnClickListener {
            if (isEllipsized) {
                binding.eventDescription.maxLines = Int.MAX_VALUE
                binding.scrollView.post { binding.scrollView.smoothScrollBy(0, 150) }
                binding.readMore.isVisible = false
            }
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.eventDescription.viewTreeObserver.addOnGlobalLayoutListener {
            val layout = binding.eventDescription.layout
            isEllipsized = layout.getEllipsisCount(layout.lineCount - 1) > 0
            binding.readMore.isVisible = isEllipsized
        }
    }
}