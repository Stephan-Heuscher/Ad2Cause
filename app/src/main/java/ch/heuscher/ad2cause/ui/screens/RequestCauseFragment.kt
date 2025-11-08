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

                // Create cause with Firebase - call repository directly to get Firestore ID
                val newCause = ch.heuscher.ad2cause.data.models.Cause(
                    name = name,
                    description = description,
                    category = category,
                    imageUrl = imageUrl,
                    isUserAdded = true,
                    totalEarned = 0.0,
                    status = ch.heuscher.ad2cause.data.models.CauseStatus.PENDING.name,
                    createdBy = userId
                )

                val database = ch.heuscher.ad2cause.data.database.Ad2CauseDatabase.getDatabase(requireContext())
                val repository = ch.heuscher.ad2cause.data.repository.CauseRepository(
                    database.causeDao(),
                    firebaseRepository
                )

                val createResult = repository.createUserCause(newCause, userId, imageUrl)

                if (createResult.isSuccess) {
                    val firestoreId = createResult.getOrNull()!!
                    android.util.Log.d("RequestCauseFragment", "Cause created with Firestore ID: $firestoreId")

                    // Verify the data was written by reading it back
                    kotlinx.coroutines.delay(1000) // Wait 1 second for write to complete

                    val verificationResult = firebaseRepository.getCauseById(firestoreId)

                    if (verificationResult.isSuccess) {
                        val verifiedCause = verificationResult.getOrNull()
                        android.util.Log.d("RequestCauseFragment", "✓ VERIFICATION SUCCESS: Cause found in Firestore: ${verifiedCause?.name}")
                        Toast.makeText(
                            requireContext(),
                            "✓ SUCCESS: ${getString(R.string.cause_submitted)}\nVerified in Firestore!",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        android.util.Log.e("RequestCauseFragment", "✗ VERIFICATION FAILED: Could not read cause from Firestore")
                        Toast.makeText(
                            requireContext(),
                            "⚠ WARNING: Cause submitted but not found in Firestore.\nCheck Firebase security rules!",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    // Navigate back
                    findNavController().navigateUp()
                } else {
                    throw createResult.exceptionOrNull() ?: Exception("Failed to create cause")
                }
            } catch (e: Exception) {
                // Log the error
                android.util.Log.e("RequestCauseFragment", "Failed to create cause", e)

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
