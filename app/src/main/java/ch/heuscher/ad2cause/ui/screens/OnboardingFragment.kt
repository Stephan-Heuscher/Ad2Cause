package ch.heuscher.ad2cause.ui.screens

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import ch.heuscher.ad2cause.R

/**
 * Simple Onboarding flow using ViewPager2
 */
class OnboardingFragment : Fragment() {

    private val PREF_ONBOARDING_SEEN = "pref_onboarding_seen"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_onboarding, container, false)

        val pager = view.findViewById<ViewPager2>(R.id.onboardingPager)
        pager.adapter = OnboardingAdapter()

        val btnNext = view.findViewById<View>(R.id.onboardingNext)
        val btnSkip = view.findViewById<View>(R.id.onboardingSkip)

        btnNext.setOnClickListener {
            if (pager.currentItem < (pager.adapter?.itemCount ?: 1) - 1) {
                pager.currentItem = pager.currentItem + 1
            } else {
                finishOnboarding()
            }
        }

        btnSkip.setOnClickListener {
            finishOnboarding()
        }

        return view
    }

    private fun finishOnboarding() {
        requireContext().getSharedPreferences("ad2cause_prefs", Context.MODE_PRIVATE)
            .edit().putBoolean(PREF_ONBOARDING_SEEN, true).apply()

        // Navigate back to home
        try {
            findNavController().navigate(R.id.nav_home)
        } catch (e: Exception) {
            parentFragmentManager.popBackStack()
        }
    }

    private inner class OnboardingAdapter : RecyclerView.Adapter<OnboardingViewHolder>() {
        private val pages = listOf(
            R.layout.onboarding_page_1,
            R.layout.onboarding_page_2,
            R.layout.onboarding_page_3
        )

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
            val v = layoutInflater.inflate(viewType, parent, false)
            return OnboardingViewHolder(v)
        }

        override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
            // static content in xml pages
        }

        override fun getItemViewType(position: Int): Int = pages[position]

        override fun getItemCount(): Int = pages.size
    }

    private class OnboardingViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
