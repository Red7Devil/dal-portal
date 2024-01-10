package com.example.dalportal.ui.feedback

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.dalportal.R

class FeedbackFragment : Fragment() {

    private lateinit var nameEditText: EditText
    private lateinit var professorNameEditText: EditText
    private lateinit var openEndedQuestionEditText: EditText
    private lateinit var courseSpinner: Spinner
    private lateinit var satisfactionSpinner: Spinner
    private lateinit var materialsSpinner: Spinner
    private lateinit var effectivenessSpinner: Spinner
    private lateinit var submitButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_feedback, container, false)

        nameEditText = view.findViewById(R.id.etYourName)
        professorNameEditText = view.findViewById(R.id.etProfessorName)
        openEndedQuestionEditText = view.findViewById(R.id.etOpenEndedQuestion)
        courseSpinner = view.findViewById(R.id.spinnerSelectCourse)
        satisfactionSpinner = view.findViewById(R.id.spinnerQuestion1)
        materialsSpinner = view.findViewById(R.id.spinnerQuestion2)
        effectivenessSpinner = view.findViewById(R.id.spinnerQuestion3)
        submitButton = view.findViewById(R.id.btnSubmitFeedback)

        submitButton.setOnClickListener {
            submitFeedback()
        }

        return view
    }

    private fun submitFeedback() {
        if (validateFields()) {
            // Code to handle feedback submission
            Toast.makeText(context, "Feedback submitted", Toast.LENGTH_SHORT).show()
            resetFields()
        } else {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateFields(): Boolean {
        return nameEditText.text.isNotBlank() &&
                professorNameEditText.text.isNotBlank() &&
                openEndedQuestionEditText.text.isNotBlank()
    }

    private fun resetFields() {
        nameEditText.text.clear()
        professorNameEditText.text.clear()
        openEndedQuestionEditText.text.clear()
        courseSpinner.setSelection(0)
        satisfactionSpinner.setSelection(0)
        materialsSpinner.setSelection(0)
        effectivenessSpinner.setSelection(0)
    }
}
