package com.example.dalportal.ui.assignments_review.add_assignment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dalportal.R
import com.example.dalportal.model.Assignment
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class AddAssignmentFragment : Fragment() {
    private lateinit var textAssignmentName: TextView
    private lateinit var textDescription: TextView
    private lateinit var textStudentId: TextView
    private lateinit var addAssignmentButton: Button
    private lateinit var textAssignmentDate: TextView
    private lateinit var textAssignmentTime: TextView
    private val assignment = Assignment()

    private var studentList: MutableList<UserDetails> = mutableListOf()
    private var studentNameList: MutableList<String?> = mutableListOf()
    private var selectedStudents: MutableList<Boolean> = mutableListOf()
    private var selectedStudentsList: MutableList<UserDetails> = mutableListOf()
    private var deadlineDate: Calendar = Calendar.getInstance()

    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        db = FirebaseFirestore.getInstance()

        return inflater.inflate(R.layout.fragment_add_assignment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textAssignmentName = view.findViewById(R.id.textAssignmentName)
        textDescription = view.findViewById(R.id.textDescription)
        textStudentId = view.findViewById(R.id.textStudentId)
        addAssignmentButton = view.findViewById(R.id.addAssignmentBtn)
        textAssignmentDate = view.findViewById(R.id.assignmentDate)
        textAssignmentTime = view.findViewById(R.id.assignmentTime)

        assignment.completionStatus = "Pending"

        textStudentId.setOnClickListener {
            showStudentsDialog()
        }

        textAssignmentDate.setOnClickListener {
            var c: Calendar = Calendar.getInstance()
            if (deadlineDate != null) {
                c = deadlineDate as Calendar
            }
            var mYear = c.get(Calendar.YEAR)
            var mMonth = c.get(Calendar.MONTH)
            var mDay = c.get(Calendar.DAY_OF_MONTH)


            val datePickerDialog = DatePickerDialog(
                view.context,
                { view, year, monthOfYear, dayOfMonth ->
                    val date: Calendar = Calendar.getInstance()
                    date.set(year, monthOfYear, dayOfMonth)
                    var formatter = SimpleDateFormat("yyyy-MM-dd")
                    deadlineDate = date
                    textAssignmentDate.text =
                        formatter.format(date.time)
                }, mYear, mMonth, mDay
            )
            datePickerDialog.show()

        }

        textAssignmentTime.setOnClickListener {
            var c: Calendar = Calendar.getInstance()
            if (deadlineDate != null) {
                c = deadlineDate
            }
            val mHour = c.get(Calendar.HOUR_OF_DAY);
            val mMinute = c.get(Calendar.MINUTE);

            val timePickerDialog = TimePickerDialog(
                view.context,
                { view, hourOfDay, minute ->
                    deadlineDate?.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    deadlineDate?.set(Calendar.MINUTE, mMinute)
                    var time = ""

                    time = if (minute < 10) {
                        " : 0$minute"
                    } else {
                        " : $minute"
                    }

                    time = if (hourOfDay == 0) {
                        "12$time am"
                    } else if (hourOfDay > 12) {
                        "${hourOfDay % 12}$time pm"
                    } else {
                        "$hourOfDay$time am"
                    }



                    textAssignmentTime.text = time
                },
                mHour,
                mMinute,
                false
            )
            timePickerDialog.show()
        }

        addAssignmentButton.setOnClickListener {
            val name = textAssignmentName.text
            val desc = textDescription.text
            val students = selectedStudentsList
            var formatter = SimpleDateFormat("dd MMMM, yyyy hh:mm a", Locale.US)
            val deadline = formatter.format(deadlineDate.time)

            if (name.isEmpty() || students.isEmpty()) {
                Toast.makeText(context, "All fields required", Toast.LENGTH_SHORT).show()
            } else if (Calendar.getInstance().after(deadlineDate)) {
                Toast.makeText(context, "Deadline cannot be past date", Toast.LENGTH_SHORT).show()
            } else {
                addAssignmentButton.isClickable = false
                assignment.name = name.toString()
                assignment.description = desc.toString()
                assignment.deadline = deadline
                for ((index, student) in students.withIndex()) {
                    assignment.studentId = student.id!!
                    db.collection("assignments").add(assignment).addOnSuccessListener { docRef ->
                        if (index == students.size - 1) {
                            addAssignmentButton.isClickable = true
                            findNavController().navigate(R.id.action_addAssignment_to_assignmentReview)
                        }
                    }

                }
            }
        }

    }

    private fun showStudentsDialog() {
        db.collection("users").whereEqualTo("role", "Student").get()
            .addOnSuccessListener { documents ->
                selectedStudents.clear()
                studentList.clear()
                studentNameList.clear()
                for (document in documents) {
                    val user = document.toObject(UserDetails::class.java)
                    studentList.add(user)
                    studentNameList.add("${user.name}(${user.id})")
                    if (selectedStudentsList.contains(user)) {
                        selectedStudents.add(true)
                    } else {
                        selectedStudents.add(false)
                    }
                }

                val builder = AlertDialog.Builder(activity)
                builder.setTitle("Select students")
                builder.setMultiChoiceItems(
                    studentNameList.toTypedArray(), selectedStudents.toBooleanArray()
                ) { dialog, which, isChecked ->
                    selectedStudents[which] = isChecked
                }

                builder.setPositiveButton("Save") { dialog, i ->
                    selectedStudentsList.clear()
                    var studentsString: String = ""
                    for ((index, isSelected) in selectedStudents.withIndex()) {
                        if (isSelected) {
                            selectedStudentsList.add(studentList[index])
                            studentsString += studentNameList[index] + ", "
                        }
                    }
                    textStudentId.text = studentsString
                }
                builder.setNegativeButton("Cancel") { dialog, i ->
                    selectedStudents.clear()
                }

                builder.show()
            }
    }
}