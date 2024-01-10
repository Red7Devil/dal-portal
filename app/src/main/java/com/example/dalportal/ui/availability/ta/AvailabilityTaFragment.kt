package com.example.dalportal.ui.availability.ta

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import com.example.dalportal.R
import com.example.dalportal.ui.availability.ta.adapter.TabViewAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class AvailabilityTaFragment : Fragment() {

    private lateinit var tabViewAdapter: TabViewAdapter
    private lateinit var viewPager: ViewPager2

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            // Handle the back button press here
            // You can add your custom logic or call the fragment's onBackPressed method
            // For example:
//             popBackStack() //to navigate back
            val fm: FragmentManager = requireActivity().supportFragmentManager
            fm.popBackStack()
            fm.popBackStack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        // Remove the callback when the fragment is destroyed
        onBackPressedCallback.remove()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment



        return inflater.inflate(R.layout.fragment_availability_ta, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tabViewAdapter = TabViewAdapter(this)
        viewPager = view.findViewById(R.id.ava_ta_pager)
        viewPager.adapter = tabViewAdapter
        val tabLayout: TabLayout = view.findViewById(R.id.tabLayout)

        // TabLayoutMediator to set tab names based on the current province
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = arrayOf("Add", "View").get(position)
        }.attach()


    }

}