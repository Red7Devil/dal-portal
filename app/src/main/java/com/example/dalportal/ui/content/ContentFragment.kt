package com.example.dalportal.ui.content

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.dalportal.databinding.FragmentContentBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.util.*

class ContentFragment : Fragment() {

    private var _binding: FragmentContentBinding? = null
    private val binding get() = _binding!!

    // Placeholder for the selected file URI
    private var selectedFileUri: Uri? = null

    // Firebase Storage
    private val storage = Firebase.storage
    private val storageRef: StorageReference = storage.reference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Placeholder button for file upload
        val btnUploadFile: Button = binding.btnUploadFile
        btnUploadFile.setOnClickListener {
            launchFilePicker()
        }

        // Submit button to upload the file
        val btnSubmit: Button = binding.btnSubmit
        btnSubmit.setOnClickListener {
            uploadFileToFirebaseStorage()
        }

        return root
    }

    private fun launchFilePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        startActivityForResult(intent, FILE_PICKER_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let {
                // File selected by the user
                selectedFileUri = it
                // Display the selected file name in the UI
                val selectedFileName = getFileName(it)
                binding.textSelectedFile.text = "Selected File: $selectedFileName"
                binding.textSelectedFile.visibility = View.VISIBLE
            }
        }
    }

    private fun getFileName(uri: Uri): String? {
        val cursor = requireActivity().contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            it.moveToFirst()
            return it.getString(nameIndex)
        }
        return null
    }

    private fun uploadFileToFirebaseStorage() {
        if (selectedFileUri != null) {
            val uniqueFileName = "${UUID.randomUUID()}_${selectedFileUri?.lastPathSegment}"
            val fileRef: StorageReference = storageRef.child("content/$uniqueFileName")

            fileRef.putFile(selectedFileUri!!)
                .addOnSuccessListener {
                    // File uploaded successfully
                    showSuccessAlert()
                }
                .addOnFailureListener { exception ->
                    // Handle unsuccessful file upload
                    showErrorAlert(exception.message)
                }
        } else {
            // No file selected
            showNoFileSelectedError()
        }
    }

    private fun showSuccessAlert() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("File Uploaded Successfully!")
        builder.setPositiveButton("OK") { dialog, which ->
            // Dismiss the dialog
            dialog.dismiss()
            // Reset the text and hide visibility of the selected file TextView
            binding.textSelectedFile.text = ""
            binding.textSelectedFile.visibility = View.GONE
        }
        builder.show()
    }

    private fun showErrorAlert(message: String?) {
        showAlert("Error", message)
    }

    private fun showAlert(title: String, message: String?) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, which -> dialog.dismiss() }
        builder.show()
    }


    private fun showNoFileSelectedError() {
        showAlert("Error", "No file selected")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val FILE_PICKER_REQUEST_CODE = 1
    }
}
