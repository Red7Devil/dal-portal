package com.example.dalportal.ui.ta_portal

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Adapter class for managing ta portal fragments in a ViewPager.
 * Inherits from FragmentStateAdapter.
 *
 * @param fragmentManager FragmentManager that will interact with this adapter
 * @param lifecycle Lifecycle of the fragments managed by this adapter
 */
class TaPortalViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    /**
     * Returns the count of fragments managed by this adapter.
     * Always returns 2 in this case, as we are dealing with two ta portal fragments.
     */
    override fun getItemCount(): Int {
        return 1
    }

    /**
     * Creates and returns the fragment for the given position.
     * This function decides which fragment to display based on the current position.
     *
     * @param position The current position in the ViewPager
     * @return Fragment that corresponds to the current position
     */
    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return TaPortalHomeFragment()
        }

        return TaPortalHomeFragment()
    }

}