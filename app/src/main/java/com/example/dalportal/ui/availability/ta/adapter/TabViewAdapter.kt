package com.example.dalportal.ui.availability.ta.adapter

// Importing necessary classes and packages
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.dalportal.ui.availability.prof.AvailabilityProfFragment
import com.example.dalportal.ui.availability.ta.AvailabilityTaAddFragment
import com.example.dalportal.ui.availability.ta.AvailabilityTaTimeOffFragment
import com.example.dalportal.ui.availability.ta.AvailabilityTaViewFragment
import com.example.dalportal.util.UserData

class TabViewAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val userData = UserData
    // Number of city fragments to be managed by the adapter
    private val fragmentCount = 2

    // Override method to get the total number of fragments
    override fun getItemCount(): Int {
        if(userData.role=="Professor")
            return 1
        if(userData.role=="TA")
            return 2
        return 2
    }

    // Override method to create and return a city fragment based on its position
    override fun createFragment(position: Int): Fragment {
        // Using a when statement to determine the fragment based on position
        if(userData.role=="Professor")
        {
            when(position) {
                0-> {
                    return AvailabilityProfFragment()
                }
            }

            return AvailabilityProfFragment()
        }

        if(userData.role=="TA") {
            when (position) {
                0 -> {
                    // Creating and returning City1Fragment for position 0
                    return AvailabilityTaAddFragment()
                }

                1 -> {
                    // Creating and returning City2Fragment for position 1
                    return AvailabilityTaViewFragment()
                }

            }

            // Default case: return City1Fragment if position is not 0, 1, or 2
            return AvailabilityTaAddFragment()
        }

        when (position) {
            0 -> {
                // Creating and returning City1Fragment for position 0
                return AvailabilityTaAddFragment()
            }

            1 -> {
                // Creating and returning City2Fragment for position 1
                return AvailabilityTaViewFragment()
            }

        }

        return AvailabilityTaAddFragment()
    }
}
