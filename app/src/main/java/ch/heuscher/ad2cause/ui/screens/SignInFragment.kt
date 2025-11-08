package ch.heuscher.ad2cause.ui.screens

import android.app.Activity
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import ch.heuscher.ad2cause.R
import ch.heuscher.ad2cause.databinding.FragmentSignInBinding
import ch.heuscher.ad2cause.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch

/**
 * Sign-In Fragment
 * Provides Google Sign-In and Email/Password authentication
 */
class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private lateinit var authViewModel: AuthViewModel

    // Google Sign-In launcher
    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            authViewModel.handleGoogleSignInResult(task)
        } else {
            Toast.makeText(requireContext(), R.string.sign_in_error, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        authViewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]

        setupUI()
        observeAuthState()
    }

    private fun setupUI() {
        // Email/Password Sign In
        binding.signInButton.setOnClickListener {
            signInWithEmail()
        }

        // Create Account
        binding.createAccountButton.setOnClickListener {
            createAccountWithEmail()
        }

        // Google Sign-In
        binding.googleSignInButton.setOnClickListener {
            signInWithGoogle()
        }
    }

    private fun signInWithEmail() {
        val email = binding.emailInput.text.toString().trim()
        val password = binding.passwordInput.text.toString()

        if (!validateInput(email, password)) {
            return
        }

        authViewModel.signInWithEmailPassword(email, password)
    }

    private fun createAccountWithEmail() {
        val email = binding.emailInput.text.toString().trim()
        val password = binding.passwordInput.text.toString()

        if (!validateInput(email, password)) {
            return
        }

        authViewModel.createAccountWithEmailPassword(email, password)
    }

    private fun validateInput(email: String, password: String): Boolean {
        // Clear previous errors
        binding.emailInputLayout.error = null
        binding.passwordInputLayout.error = null

        // Validate email
        if (email.isEmpty()) {
            binding.emailInputLayout.error = getString(R.string.fields_required)
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailInputLayout.error = getString(R.string.invalid_email)
            return false
        }

        // Validate password
        if (password.isEmpty()) {
            binding.passwordInputLayout.error = getString(R.string.fields_required)
            return false
        }

        if (password.length < 6) {
            binding.passwordInputLayout.error = getString(R.string.password_too_short)
            return false
        }

        return true
    }

    private fun signInWithGoogle() {
        val signInIntent = authViewModel.getGoogleSignInClient().signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    private fun observeAuthState() {
        // Observe sign-in result
        lifecycleScope.launch {
            authViewModel.signInResult.observe(viewLifecycleOwner) { result ->
                result?.let {
                    if (it.isSuccess) {
                        Toast.makeText(
                            requireContext(),
                            "Signed in successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                        // Navigate back to home
                        findNavController().navigateUp()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            it.exceptionOrNull()?.message ?: getString(R.string.sign_in_error),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    authViewModel.resetEvents()
                }
            }
        }

        // Observe loading state
        authViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.loadingIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.signInButton.isEnabled = !isLoading
            binding.createAccountButton.isEnabled = !isLoading
            binding.googleSignInButton.isEnabled = !isLoading
        }

        // Check if already signed in
        lifecycleScope.launch {
            authViewModel.isSignedIn.collect { isSignedIn ->
                if (isSignedIn) {
                    // Navigate back if already signed in
                    findNavController().navigateUp()
                }
            }
        }
    }
}
