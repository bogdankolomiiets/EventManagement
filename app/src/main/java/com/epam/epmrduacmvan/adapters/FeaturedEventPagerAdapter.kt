package com.epam.epmrduacmvan.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.epam.epmrduacmvan.model.Event
import com.epam.epmrduacmvan.views.FeaturedEventFragment

class FeaturedEventPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var fragmentList: MutableList<Fragment> = ArrayList()

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    fun createFragments(listEvent: List<Event>) {
        listEvent.forEach { fragmentList.add(bindFeatheredEvent(it)) }
    }

    private fun bindFeatheredEvent(event: Event): Fragment {
        val fragment = FeaturedEventFragment()
        fragment.bindEvent(event)
        return fragment
    }
}