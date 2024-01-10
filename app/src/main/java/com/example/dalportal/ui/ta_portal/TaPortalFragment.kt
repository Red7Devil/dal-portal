package com.example.dalportal.ui.ta_portal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.dalportal.R
import com.example.dalportal.databinding.FragmentTaPortalBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class TaPortalFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabs: TabLayout
    private lateinit var viewPagerAdapter: TaPortalViewPagerAdapter
    private val tabsList = arrayOf("Tasks")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val taPortalViewModel =
            ViewModelProvider(this).get(TaPortalViewModel::class.java)

        val view: View = inflater.inflate(R.layout.fragment_ta_portal, container, false)

        viewPager = view.findViewById(R.id.taViewPager)
        tabs = view.findViewById(R.id.taTabLayout)
        viewPagerAdapter = TaPortalViewPagerAdapter(childFragmentManager, lifecycle)
        viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = tabsList[position]
        }.attach()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}