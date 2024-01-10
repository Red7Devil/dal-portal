package com.example.dalportal.ui.availability

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dalportal.R
import com.example.dalportal.ui.availability.prof.AvailabilityProfFragment
import com.example.dalportal.ui.availability.ta.AvailabilityTaFragment

class AvailabilityFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_availability, container, false)

        val view = inflater.inflate(R.layout.fragment_availability, container, false)

        return view
    }

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Replace FirstFragment with SecondFragment
        val availabilityTaFragment = AvailabilityTaFragment()
        val availabilityProfFragment = AvailabilityProfFragment()

        val transaction = requireActivity().supportFragmentManager.beginTransaction()

        var role = "Professor"
        role = "TA"
        if(role=="Professor") {
            transaction.replace(R.id.nav_host_fragment_content_main, availabilityProfFragment)
        }
        else{
            transaction.replace(R.id.nav_host_fragment_content_main, availabilityTaFragment)
        }

        transaction.addToBackStack(null) // Optional: Add transaction to back stack
        transaction.commit()


    }


}