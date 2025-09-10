package com.project.novaiptv

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.project.novaiptv.databinding.LandingPageBinding // Assuming ViewBinding

class LandingActivity : AppCompatActivity() {

    private lateinit var binding: LandingPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LandingPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Example: Setup a button to show the overlay
        // Replace this with your actual trigger for showing the overlay
        binding.footerButtonRight2Landing.setOnClickListener { // Assuming settings button shows overlay
            showOverlay()
        }

        binding.overlayCloseButton.setOnClickListener {
            hideOverlay()
        }

        // Initially hide the overlay if it's not meant to be shown at start
        binding.overlayContainer.visibility = View.GONE
    }

    private fun showOverlay() {
        binding.overlayContainer.visibility = View.VISIBLE
        // Request focus on the element you want to be selected first in the overlay
        binding.overlayCloseButton.requestFocus()

        // Disable focus for all children of the main layout behind the overlay
        setChildrenFocusable(binding.mainConstraintLayout, false, binding.overlayContainer)
    }

    private fun hideOverlay() {
    binding.overlayContainer.visibility = View.GONE

    // Re-enable focus and enabled state for all children of the main layout
    setChildrenFocusable(binding.mainConstraintLayout, true, binding.overlayContainer)

    // --- Key change: Explicitly request focus on a known element in the main layout ---
    // For example, let's set focus back to the settings button that opened the overlay.
    // Replace 'binding.footerButtonRight2Landing' with the element
    // you want to receive focus after the overlay closes.
    binding.footerButtonRight2Landing.requestFocus()
    // Alternatively, if you want a more general approach:
    // binding.mainConstraintLayout.requestFocus()
    // This will try to find the first focusable element in the main layout.
    // However, explicitly setting it to a known button is often more reliable for TV.
}


    /**
     * Recursively sets the focusable and enabled state for all child views of a ViewGroup,
     * skipping a specific view (e.g., the overlay itself).
     */
    private fun setChildrenFocusable(parent: ViewGroup, focusable: Boolean, skipView: View?) {
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            if (child == skipView) { // Don't disable the overlay itself
                continue
            }
            child.isFocusable = focusable
            child.isEnabled = focusable // Often good to disable them too
            if (child is ViewGroup) {
                setChildrenFocusable(child, focusable, skipView) // Recurse for nested ViewGroups
            }
        }
    }
}
