import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.dalportal.MainActivity
import com.example.dalportal.R
import com.example.dalportal.databinding.FragmentAssignmentDetailBinding
import com.example.dalportal.ui.assignments.AssignmentViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class AssignmentDetailFragment : Fragment() {

    companion object {
        private const val ARG_ASSIGNMENT_NAME = "assignmentName"
        private const val ARG_ASSIGNMENT_DEADLINE = "assignmentDeadline"
        private var selectedFileName: String? = null

        // Request code for file picker
        private const val FILE_PICKER_REQUEST_CODE = 1

        fun newInstance(
            assignmentName: String?,
            assignmentDeadline: String?
        ): AssignmentDetailFragment {
            val fragment = AssignmentDetailFragment()
            val args = Bundle()
            args.putString(ARG_ASSIGNMENT_NAME, assignmentName)
            args.putString(ARG_ASSIGNMENT_DEADLINE, assignmentDeadline)
            fragment.arguments = args
            return fragment
        }
    }

    private var _binding: FragmentAssignmentDetailBinding? = null
    private val binding get() = _binding!!

    private var selectedFileUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAssignmentDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Retrieve assignment details from arguments
        val assignmentName = arguments?.getString(ARG_ASSIGNMENT_NAME)
        val assignmentDeadline = arguments?.getString(ARG_ASSIGNMENT_DEADLINE)

        // Set assignment details in the UI
        binding.assignmentDetails.text = "$assignmentName\n$assignmentDeadline"

        // Initialize Firebase storage
        val storage = Firebase.storage
        val storageRef: StorageReference = storage.reference

        // Button to pick a file
        val pickFileButton: Button = binding.pickFileButton
        pickFileButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "*/*"
            startActivityForResult(intent, FILE_PICKER_REQUEST_CODE)
        }

// Button to submit the file
        val submitButton: Button = binding.submitButton
        submitButton.setOnClickListener {
            // Check if a file is selected
            if (selectedFileUri != null) {
                // Upload the file to Firebase Storage
                val fileName = selectedFileUri?.lastPathSegment
                val assignmentName = arguments?.getString(ARG_ASSIGNMENT_NAME)

                // Retrieve the studentId from the assignmentViewModel or any other source
                val studentId = getStudentIdFromAssignmentViewModel(assignmentName)

                if (assignmentName != null && studentId != null) {
                    Toast.makeText(
                        context,
                        "Uploading file. It will take a few minutes",
                        Toast.LENGTH_LONG
                    ).show()
                    val fileRef: StorageReference =
                        storageRef.child("/$assignmentName/$studentId/$fileName")
                    fileRef.putFile(selectedFileUri!!)
                        .continueWithTask { uploadTask ->
                            if (!uploadTask.isSuccessful) {
                                throw uploadTask.exception!!
                            }
                            return@continueWithTask fileRef.downloadUrl
                        }
                        .addOnSuccessListener { downloadUri ->
                            // File uploaded successfully
                            val fileUrl = downloadUri.toString()
                            updateCompletionStatusAndFileUrlInFirestore("Completed", fileUrl)
                            showSuccessAlert()
                        }
                        .addOnFailureListener { exception ->
                            // Handle unsuccessful uploads
                            showErrorAlert(exception.message)
                        }
                }
            } else {
                // No file selected, show error message
                showNoFileSelectedError()
            }
        }

        return root
    }

    private fun showSuccessAlert() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("File Uploaded Successfully!")
        builder.setPositiveButton("OK") { dialog, which ->
            // Dismiss the dialog
            dialog.dismiss()
            // Navigate back to the previous page
            activity?.onBackPressed()
        }
        builder.show()
    }


    private fun showErrorAlert(message: String?) {
        showAlert("Error", message)
    }

    private fun showNoFileSelectedError() {
        showAlert("Error", "Please select a file")
    }

    private fun showAlert(title: String, message: String?) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, which -> dialog.dismiss() }
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let {
                // File selected by the user
                selectedFileUri = it
                // Update the selected file name in the UI
                selectedFileName = getFileName(it)
                binding.selectedFileName.text = selectedFileName
                binding.selectedFileName.visibility = View.VISIBLE
                // TODO: Update UI to show the selected file name or other information
            }
        }
    }

    // Helper function to get the file name from a Uri
    private fun getFileName(uri: Uri): String? {
        val cursor = requireActivity().contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            it.moveToFirst()
            return it.getString(nameIndex)
        }
        return null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Function to retrieve studentId from the AssignmentViewModel or any other source
    private fun getStudentIdFromAssignmentViewModel(assignmentName: String?): String? {
        val assignmentViewModel =
            ViewModelProvider(requireActivity()).get(AssignmentViewModel::class.java)
        val assignments = assignmentViewModel.assignments.value
        val assignment = assignments?.find { it.name == assignmentName }
        return assignment?.studentId
    }

    // Function to update completion status and file URL in Firestore
    private fun updateCompletionStatusAndFileUrlInFirestore(status: String, fileUrl: String) {
        // Retrieve assignment details from arguments
        val assignmentName = arguments?.getString(ARG_ASSIGNMENT_NAME)

        // Update completion status in Firestore
        val db = FirebaseFirestore.getInstance()
        val assignmentsCollection = db.collection("assignments")

        assignmentName?.let {
            assignmentsCollection
                .whereEqualTo("name", it)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot.documents) {
                        val assignmentId = document.id
                        assignmentsCollection.document(assignmentId)
                            .update(
                                mapOf(
                                    "completionStatus" to status,
                                    "fileUrl" to fileUrl
                                )
                            )
                            .addOnSuccessListener {
                            }
                            .addOnFailureListener { exception ->
                                // Handle error updating completion status in Firestore
                                showErrorAlert(exception.message)
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle error fetching assignment document from Firestore
                    showErrorAlert(exception.message)
                }
        }
    }

}
