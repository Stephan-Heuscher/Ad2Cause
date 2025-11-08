package ch.heuscher.ad2cause.ui.screens

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import ch.heuscher.ad2cause.R
import ch.heuscher.ad2cause.data.repository.FirebaseRepository
import ch.heuscher.ad2cause.databinding.FragmentRequestCauseBinding
import ch.heuscher.ad2cause.viewmodel.AuthViewModel
import ch.heuscher.ad2cause.viewmodel.CauseViewModel
import kotlinx.coroutines.launch

/**
 * Request Cause Fragment
 * Allows users to submit new cause requests for approval.
 */
class RequestCauseFragment : Fragment() {

    private lateinit var binding: FragmentRequestCauseBinding
    private lateinit var causeViewModel: CauseViewModel
    private lateinit var authViewModel: AuthViewModel
    private val firebaseRepository = FirebaseRepository()
    private var selectedImageUri: Uri? = null

    // Category options
    private val categories = arrayOf(
        "Education",
        "Health",
        "Environment",
        "Technology",
        "Animal Welfare",
        "Poverty Relief",
        "Human Rights",
        "Arts & Culture",
        "Disaster Relief",
        "Other"
    )

    // Image picker launcher
    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            binding.selectedImagePreview.setImageURI(it)
            binding.selectedImagePreview.visibility = View.VISIBLE
            binding.imageRequiredText.visibility = View.GONE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRequestCauseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        causeViewModel = ViewModelProvider(requireActivity())[CauseViewModel::class.java]
        authViewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]

        setupCategoryDropdown()
        setupImageSelection()
        setupButtons()
    }

    /**
     * Setup category dropdown with predefined options
     */
    private fun setupCategoryDropdown() {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, categories)
        binding.causeCategoryDropdown.setAdapter(adapter)
    }

    /**
     * Setup image selection
     */
    private fun setupImageSelection() {
        binding.selectImageButton.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }
    }

    /**
     * Setup button listeners
     */
    private fun setupButtons() {
        binding.cancelButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.submitCauseButton.setOnClickListener {
            submitCauseRequest()
        }
    }

    /**
     * Validate and submit cause request
     */
    private fun submitCauseRequest() {
        val name = binding.causeNameInput.text.toString().trim()
        val category = binding.causeCategoryDropdown.text.toString().trim()
        val description = binding.causeDescriptionInput.text.toString().trim()

        // Clear previous errors
        binding.causeNameInputLayout.error = null
        binding.causeCategoryInputLayout.error = null
        binding.causeDescriptionInputLayout.error = null
        binding.imageRequiredText.visibility = View.GONE

        // Validate inputs
        var hasError = false

        if (name.isEmpty()) {
            binding.causeNameInputLayout.error = "Name is required"
            hasError = true
        } else if (name.length < 3) {
            binding.causeNameInputLayout.error = "Name must be at least 3 characters"
            hasError = true
        }

        if (category.isEmpty()) {
            binding.causeCategoryInputLayout.error = "Category is required"
            hasError = true
        }

        if (description.isEmpty()) {
            binding.causeDescriptionInputLayout.error = "Description is required"
            hasError = true
        } else if (description.length < 10) {
            binding.causeDescriptionInputLayout.error = "Description must be at least 10 characters"
            hasError = true
        }

        if (selectedImageUri == null) {
            binding.imageRequiredText.visibility = View.VISIBLE
            hasError = true
        }

        if (hasError) {
            return
        }

        // Check if user is signed in
        val userId = authViewModel.getCurrentUserId()
        if (userId == null) {
            Toast.makeText(
                requireContext(),
                "Please sign in to request causes",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        // Disable button while uploading
        binding.submitCauseButton.isEnabled = false
        binding.submitCauseButton.text = getString(R.string.uploading_image)

        // Upload image and create cause
        lifecycleScope.launch {
            try {
                // Upload image to Firebase Storage
                val imageResult = firebaseRepository.uploadCauseImage(selectedImageUri!!)
                if (imageResult.isFailure) {
                    throw imageResult.exceptionOrNull() ?: Exception("Image upload failed")
                }

                val imageUrl = imageResult.getOrNull()!!

                // Create cause with Firebase
                causeViewModel.addNewUserCause(name, description, category, imageUrl, userId)

                Toast.makeText(
                    requireContext(),
                    getString(R.string.cause_submitted),
                    Toast.LENGTH_LONG
                ).show()

                // Navigate back
                findNavController().navigateUp()
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.image_upload_error) + ": ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
                binding.submitCauseButton.isEnabled = true
                binding.submitCauseButton.text = getString(R.string.save_cause)
            }
        }
    }
}
