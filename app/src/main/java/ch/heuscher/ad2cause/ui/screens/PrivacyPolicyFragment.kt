package ch.heuscher.ad2cause.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import ch.heuscher.ad2cause.databinding.FragmentPrivacyPolicyBinding

/**
 * Privacy Policy Fragment
 * Displays the app's privacy policy in a WebView.
 */
class PrivacyPolicyFragment : Fragment() {

    private var _binding: FragmentPrivacyPolicyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPrivacyPolicyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupWebView()
        loadPrivacyPolicy()
    }

    /**
     * Configure WebView settings
     */
    private fun setupWebView() {
        binding.privacyPolicyWebView.apply {
            settings.apply {
                // Enable JavaScript if needed for interactive content
                javaScriptEnabled = false

                // Allow zooming
                setSupportZoom(true)
                builtInZoomControls = true
                displayZoomControls = false

                // Improve text rendering
                textZoom = 100

                // Enable responsive layout
                loadWithOverviewMode = true
                useWideViewPort = true
            }
        }
    }

    /**
     * Load the privacy policy HTML from assets
     */
    private fun loadPrivacyPolicy() {
        try {
            binding.privacyPolicyWebView.loadUrl("file:///android_asset/privacy_policy.html")
        } catch (e: Exception) {
            // If loading fails, show error message
            binding.privacyPolicyWebView.loadData(
                "<html><body><h2>Error Loading Privacy Policy</h2><p>Unable to load privacy policy. Please try again later.</p></body></html>",
                "text/html",
                "UTF-8"
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
