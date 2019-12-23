package com.epam.epmrduacmvan.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.epam.epmrduacmvan.AppApplication
import com.epam.epmrduacmvan.R

class DraftEventPagerAdapter(fragmentManager: FragmentManager, private val fragments: List<Fragment>):
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return AppApplication.appContext.getString(
            when (position) {
                0 -> R.string.info_title
                1 -> R.string.schedule
                2 -> R.string.speakers
                3 -> R.string.participants
                else -> R.string.tab
            }
        )
    }
}