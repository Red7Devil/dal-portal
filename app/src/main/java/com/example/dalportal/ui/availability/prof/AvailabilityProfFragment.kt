package com.example.dalportal.ui.availability.prof

import android.app.ActionBar.LayoutParams
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import androidx.activity.OnBackPressedCallback
import androidx.core.view.marginTop
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.dalportal.R
import com.example.dalportal.ui.availability.firebase.AvailabilityData
import com.example.dalportal.ui.availability.firebase.ButtonPair
import com.example.dalportal.ui.availability.firebase.convertButtonPairsToStrings
import com.example.dalportal.util.UserData
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AvailabilityProfFragment : Fragment() {

    private lateinit var btnSelectDate1: Button
    private lateinit var btnSelectDate2: Button
    private lateinit var radioGroupDays: RadioGroup
    private lateinit var etComment: EditText
    private lateinit var btnAddRangeSet: Button
    private lateinit var linearLayoutInnerTimeRanges: LinearLayout
    private lateinit var spinnerDays: Spinner
    private lateinit var spinnerAdapter: ArrayAdapter<String>
    private lateinit var spinnerUserAdapter: ArrayAdapter<String>
    private lateinit var databaseReference: DatabaseReference
    var taNamesArray = mutableListOf<String>()
    var emailNameMap = mutableMapOf<String, String>()
    val db = FirebaseFirestore.getInstance()
    var userId: String = ""


    private var timeRangeButtonsMap = mutableMapOf<String, MutableList<Pair<Button, Button>>>()

    private var selectedStartDate = getCurrentDate()
    private var selectedEndDate = getCurrentDate()
    private var comment: String = ""

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

    private fun initializeTimeRangeButtonsMap() {
        val daysOfWeek = arrayOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

        for (day in daysOfWeek) {
            timeRangeButtonsMap[day] = mutableListOf()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_availability_prof, container, false)
        // Initialize Firebase
        // Initialize Firebase storage
        databaseReference = Firebase.database.reference


        initializeTimeRangeButtonsMap()

        // Section 1
        btnSelectDate1 = view.findViewById(R.id.btnSelectDate1)
        btnSelectDate2 = view.findViewById(R.id.btnSelectDate2)

        btnSelectDate1.text = selectedStartDate
        btnSelectDate2.text = selectedEndDate

        btnSelectDate1.setOnClickListener {
            showDatePickerDialog(btnSelectDate1)
        }

        btnSelectDate2.setOnClickListener {
            showDatePickerDialog(btnSelectDate2)
        }

        // Initialize views
        spinnerDays = view.findViewById(R.id.spinnerDays)

        // Dummy data for the spinner
        val daysArray = arrayOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

        // Create an ArrayAdapter using the string array and a default spinner layout
        spinnerAdapter = ArrayAdapter(requireContext(), R.layout.spinner_items, daysArray)

        // Specify the layout to use when the list of choices appears
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        spinnerDays.adapter = spinnerAdapter


        loadDataFromFirestore(db, userId.toString(), view)


        // Set a listener to handle item selection
        spinnerDays.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                val selectedDay = spinnerAdapter.getItem(position)
                Toast.makeText(requireContext(), "Selected Day: $selectedDay", Toast.LENGTH_SHORT).show()
                linearLayoutInnerTimeRanges.removeAllViews()

                val availabilityData = AvailabilityData(
                    timeRangeButtonsMap = convertButtonPairsToStrings(timeRangeButtonsMap)
                )
                updateUI(availabilityData, selectedDay.toString())
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Do nothing here
            }
        }

        // Section 3

        linearLayoutInnerTimeRanges = view.findViewById(R.id.linearLayoutInnerTimeRanges)


        // Section 4
        etComment = view.findViewById(R.id.etComment)

        val updateButton: Button = view.findViewById(R.id.btnSubmitAvability)

        updateButton.setOnClickListener(){
            saveDataToFirebase(db)
            Toast.makeText(requireContext(), "Shifts Assigned.", Toast.LENGTH_SHORT)
        }

        return view
    }

    private fun saveDataToFirebase(db: FirebaseFirestore ) {
        // Create a unique key for the availability data
//        val availabilityKey = databaseReference.child("availability").push().key
        val convertedTimeRangeButtonsMap = convertButtonPairsToStrings(timeRangeButtonsMap)

        // Create a data object to be saved
        val availabilityData = AvailabilityData(
            userId.toString(),
            convertedTimeRangeButtonsMap,
            selectedStartDate,
            selectedEndDate,
            etComment.text.toString()
        )

        db.collection("shifts")
            .document(userId)
            .set(availabilityData)
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${userId}")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }

    }

    private fun loadDataFromFirestore(db: FirebaseFirestore, documentId: String, view: View) {
        // Step 1: Read the "users" collection
        runBlocking {
            val usersCollectionRef = db.collection("users")

//            var taNamesArray = mutableListOf<String>()
//            var emailNameMap = mutableMapOf<String, String>()

            usersCollectionRef.whereEqualTo("role", "TA")
                .get()
                .addOnSuccessListener { usersSnapshot ->
                    if (!usersSnapshot.isEmpty) {
                        for (userDocument in usersSnapshot) {
                            val email = userDocument.getString("email")
                            val name = userDocument.getString("name")

                            // Store name in the array
                            name?.let {
                                taNamesArray.add(it)
                            }

                            // Store email and name in the map
                            if (email != null && name != null) {
                                emailNameMap[name] = email
                            }
                        }

                        // Initialize views
                        var spinnerUser: Spinner = view.findViewById(R.id.spinnerUser)
                        // Dummy data for the spinner
                        val userArray = taNamesArray

                        // Create an ArrayAdapter using the string array and a default spinner layout
                        var spinnerUserAdapter =
                            ArrayAdapter(requireContext(), R.layout.spinner_items, userArray)

                        // Specify the layout to use when the list of choices appears
                        spinnerUserAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                        // Apply the adapter to the spinner
                        spinnerUser.adapter = spinnerUserAdapter

                        // Set a listener to handle item selection
                        spinnerUser.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parentView: AdapterView<*>?,
                                selectedItemView: View?,
                                position: Int,
                                id: Long
                            ) {
                                val selectedUser = spinnerUserAdapter.getItem(position)
                                Toast.makeText(requireContext(), "Selected User: $selectedUser", Toast.LENGTH_SHORT)
                                    .show()

                                getUserAva(emailNameMap[selectedUser.toString()].toString())
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                TODO("Not yet implemented")
                            }
                        }

//                        getUserAva(emailNameMap[taNamesArray[0]].toString())
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle failures while fetching users
                    Log.w("LoadData", "Error getting users: ", exception)
                }
        }
    }

    fun getUserAva(documentId:String) {
                    // Step 3: Read the document from "availability" collection
                    val docRef = db.collection("availability").document(documentId)

        userId=documentId
        linearLayoutInnerTimeRanges.removeAllViews()
                    docRef.get()
                        .addOnSuccessListener { document ->
                            if (document != null && document.exists()) {
                                val availabilityData = document.toObject(AvailabilityData::class.java)
                                // Process the retrieved data (e.g., update UI)
                                updateUI(availabilityData,"Sun")
                            } else {
                                // Document does not exist
                                // Handle the case accordingly
                            }
                        }
                        .addOnFailureListener { exception ->
                            // Handle failures
                            Log.w(ContentValues.TAG, "Error getting documents: ", exception)
                        }
    }

    private fun updateUI(availabilityData: AvailabilityData?, selectedDay: String) {
        // Update your UI with the retrieved data
        linearLayoutInnerTimeRanges.removeAllViews()
        if (availabilityData != null) {
            selectedStartDate = availabilityData.selectedStartDate
            selectedEndDate = availabilityData.selectedEndDate
            timeRangeButtonsMap = convertStringPairsToButtons(availabilityData.timeRangeButtonsMap)
            etComment.setText(availabilityData.comment)

            btnSelectDate1.text = selectedStartDate
            btnSelectDate2.text = selectedEndDate

            for (buttonPair in timeRangeButtonsMap[selectedDay]!!) {
                // Create a LinearLayout to wrap the buttons
                val availabilityDataLayout = LinearLayout(requireContext())

                availabilityDataLayout.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                )
                availabilityDataLayout.orientation = LinearLayout.VERTICAL
                availabilityDataLayout.setBackgroundColor(resources.getColor(android.R.color.holo_blue_dark))
                // Set the top margin
                val layoutParams = availabilityDataLayout.layoutParams as LinearLayout.LayoutParams
                layoutParams.topMargin = 10
                availabilityDataLayout.layoutParams = layoutParams

                val timeRangeLayout1 = LinearLayout(requireContext())
                val timeRangeLayout2 = LinearLayout(requireContext())
                timeRangeLayout1.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                timeRangeLayout1.orientation = LinearLayout.HORIZONTAL

                timeRangeLayout2.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                timeRangeLayout2.orientation = LinearLayout.HORIZONTAL

                // Create a TextView between the buttons
                val timeRangeSeparator1 = TextView(requireContext())
                timeRangeSeparator1.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                timeRangeSeparator1.text = " - "

                val timeRangeSeparator2 = TextView(requireContext())
                timeRangeSeparator2.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                timeRangeSeparator2.text = " - "

                val parentButton1Layout = buttonPair.first.parent as? ViewGroup
                parentButton1Layout?.removeView(buttonPair.first)

                val parentButton2Layout = buttonPair.second.parent as? ViewGroup
                parentButton2Layout?.removeView(buttonPair.second)

                val avTimeStart = TextView(requireContext())
                timeRangeSeparator1.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                avTimeStart.text = "Availability: "+buttonPair.first.text

                val avTimeEnd = TextView(requireContext())
                timeRangeSeparator1.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                avTimeEnd.text = buttonPair.second.text

                // Add views to the layout
                timeRangeLayout1.addView(avTimeStart)
                timeRangeLayout1.addView(timeRangeSeparator1)
                timeRangeLayout1.addView(avTimeEnd)

                buttonPair.first.text = "--:--"
                buttonPair.second.text = "--:--"

                timeRangeLayout2.addView(buttonPair.first)
                timeRangeLayout2.addView(timeRangeSeparator2)
                timeRangeLayout2.addView(buttonPair.second)

                availabilityDataLayout.addView(timeRangeLayout1)
                availabilityDataLayout.addView(timeRangeLayout2)

                // Add the layout to the parent layout
                linearLayoutInnerTimeRanges.addView(availabilityDataLayout)
            }

        }
    }

    fun convertStringPairsToButtons(buttonPairs: Map<String, List<ButtonPair>>): MutableMap<String, MutableList<Pair<Button, Button>>> {
        return buttonPairs.mapValues { (day, pairs) ->
            pairs.map { pair ->
                val btnStartTime = Button(requireContext())
                btnStartTime.layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
                )
                btnStartTime.text = pair.first
                btnStartTime.setOnClickListener {
                    showTimePickerDialog(btnStartTime, day, pair.first, pair.second)
                }

                val btnEndTime = Button(requireContext())
                btnEndTime.layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
                )
                btnEndTime.text = pair.second
                btnEndTime.setOnClickListener {
                    showTimePickerDialog(btnEndTime, day, pair.first, pair.second)
                }

                Pair(btnStartTime, btnEndTime)
            }.toMutableList()
        }.toMutableMap()
    }

    fun convertButtonsToStringPairs(buttonMap: MutableMap<String, MutableList<Pair<Button, Button>>>): Map<String, List<ButtonPair>> {
        return buttonMap.mapValues { (day, buttonPairs) ->
            buttonPairs.map { buttonPair ->
                val startTime = buttonPair.first.text.toString()
                val endTime = buttonPair.second.text.toString()
                ButtonPair(startTime, endTime)
            }
        }
    }

    fun convertStringsToButtonPairs(availabilityData: AvailabilityData): MutableMap<String, MutableList<Pair<String, String>>> {
        return availabilityData.timeRangeButtonsMap.mapValues { (_, buttonPairs) ->
            buttonPairs.map { pair ->
                val btnStartTime = pair.first
                val btnEndTime = pair.second

                Pair(btnStartTime, btnEndTime)
            }.toMutableList()
        }.toMutableMap()
    }

    private fun showDatePickerDialog(targetButton: Button) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${month + 1}/$year"

                if (targetButton.id == R.id.btnSelectDate1 && isStartDateBeforeToday(selectedDate)) {
                    Toast.makeText(requireContext(), "Start date cannot be before today", Toast.LENGTH_SHORT).show()
                    return@OnDateSetListener
                }

                if (targetButton.id == R.id.btnSelectDate1 && isStartDateAfterEndDate(selectedDate, selectedEndDate)) {
                    Toast.makeText(requireContext(), "Start date cannot be after end date", Toast.LENGTH_SHORT).show()
                    return@OnDateSetListener
                }
                if (targetButton.id == R.id.btnSelectDate2 && isStartDateAfterEndDate(selectedStartDate, selectedDate)) {
                    Toast.makeText(requireContext(), "End date cannot be before start date", Toast.LENGTH_SHORT).show()
                    return@OnDateSetListener
                }

                targetButton.text = selectedDate

                if (targetButton.id == R.id.btnSelectDate1) {
                    selectedStartDate = selectedDate
                }
                if (targetButton.id == R.id.btnSelectDate2) {
                    selectedEndDate = selectedDate
                }
            },
            year, month, day
        )

        datePickerDialog.show()
    }

    private fun isStartDateAfterEndDate(startDate: String, endDate: String): Boolean {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        try {
            val dateStart = sdf.parse(startDate)
            val dateEnd = sdf.parse(endDate)

            return dateStart != null && dateEnd != null && dateStart.after(dateEnd)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return false
    }

    private fun isStartDateBeforeToday(startDate: String): Boolean {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        try {
            val dateStart = sdf.parse(startDate)
            val dateEnd = sdf.parse(getCurrentDate())

            return dateStart != null && dateEnd != null && dateEnd.after(dateStart)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return false
    }

    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return "$day/$month/$year"
    }

//    private fun addTimeRangeSetButton() {
//        val selectedDay = spinnerDays.selectedItem.toString()
//
//        if (selectedDay.isNotEmpty()) {
//            // Create a LinearLayout to wrap the buttons
//            val timeRangeLayout = LinearLayout(requireContext())
//            timeRangeLayout.layoutParams = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//            )
//            timeRangeLayout.orientation = LinearLayout.HORIZONTAL
//
//            // Create the start time button
//            val btnStartTime = Button(requireContext())
//            btnStartTime.layoutParams = LinearLayout.LayoutParams(
//                0, // Set layout_weight to distribute space equally
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                1.0f // Set layout_weight to distribute space equally
//            )
//            btnStartTime.text = "--:--"
//            btnStartTime.setOnClickListener {
//                showTimePickerDialog(btnStartTime, selectedDay, "00:00", "")
//            }
//
//            // Create a TextView between the buttons
//            val timeRangeSeparator = TextView(requireContext())
//            timeRangeSeparator.layoutParams = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//            )
//            timeRangeSeparator.text = " - "
//
//            // Create the end time button
//            val btnEndTime = Button(requireContext())
//            btnEndTime.layoutParams = LinearLayout.LayoutParams(
//                0, // Set layout_weight to distribute space equally
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                1.0f // Set layout_weight to distribute space equally
//            )
//            btnEndTime.text = "--:--"
//            btnEndTime.setOnClickListener {
//                showTimePickerDialog(btnEndTime, selectedDay)
//            }
//
//            // Store the buttons in the map for later reference
//            timeRangeButtonsMap[selectedDay]?.add(Pair(btnStartTime, btnEndTime))
//
//            // Add views to the layout
//            timeRangeLayout.addView(btnStartTime)
//            timeRangeLayout.addView(timeRangeSeparator)
//            timeRangeLayout.addView(btnEndTime)
//
//            // Add the layout to the parent layout
//            linearLayoutInnerTimeRanges.addView(timeRangeLayout)
//
//
//        } else {
//            Toast.makeText(requireContext(), "Select a day first", Toast.LENGTH_SHORT).show()
//        }
//    }

    private fun showTimePickerDialog(targetButton: Button, selectedDay: String, avStartTime: String, avEndTime: String) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                targetButton.text = selectedTime
                if (isTimeBetween(selectedTime, avStartTime, avEndTime)) {
                    targetButton.text = selectedTime
                } else {
                    Toast.makeText(requireContext(), "Selected time is not within the range", Toast.LENGTH_SHORT).show()
                }
                if(!isStartTimeAfterEndTime(selectedDay)) {
                    Toast.makeText(requireContext(), "Start time cannot be after end time", Toast.LENGTH_SHORT).show()
                    targetButton.text = "--:--"
                }

            },
            hour, minute, true
        )

        timePickerDialog.show()

    }

    private fun isTimeBetween(selectedTime: String, startTime: String, endTime: String): Boolean {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val selectedDate = dateFormat.parse(selectedTime)
        val startDate = dateFormat.parse(startTime)
        val endDate = dateFormat.parse(endTime)

        return selectedDate in startDate..endDate
    }

    private fun isStartTimeAfterEndTime(selectedDay: String): Boolean {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())

        val timeRangeList = timeRangeButtonsMap[selectedDay]
        if (timeRangeList != null) {
            for (pair in timeRangeList) {
                val startTime = pair.first.text.toString()
                val endTime = pair.second.text.toString()

//                if(startTime=="--:--" || endTime=="--:--") return true

                try {
                    val dateStart = sdf.parse(startTime)
                    val dateEnd = sdf.parse(endTime)

                    if (dateStart != null && dateEnd != null && dateEnd.after(dateStart)) {
                        // Do nothing
                    }
                    else {
                        return false
                    }
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
            }
        }

        return true
    }


}
