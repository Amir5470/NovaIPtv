package com.project.novaiptv

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.project.novaiptv.databinding.LandingPageBinding // Assuming ViewBinding
import android.widget.button

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

        if (child == skipView) { // Don't modify the overlay container itself when it's being skipped
            // If we are enabling focus (overlay hidden), ensure overlay is NOT focusable
            if (focusable) { // This means we are HIDING the overlay
                child.isFocusable = false
                child.isEnabled = false // Also disable it to prevent clicks
            }
            continue
        }

        // Set focusable and enabled for the current child
        child.isFocusable = focusable
        child.isEnabled = focusable

        // If the child is a Button, ensure it's specifically handled (though the above should cover it)
        // This is more for emphasis and debugging, the generic child.isFocusable should work.
        if (child is Button) {
            child.isFocusable = focusable
            child.isEnabled = focusable
        }
        
        // If the child is also a ViewGroup, recurse
        if (child is ViewGroup) {
            // When we are disabling focus (overlay shown), we don't need to skip the overlay's children.
            // When we are enabling focus (overlay hidden), the skipView is the overlay, so its children are not processed here.
            setChildrenFocusable(child, focusable, null) // Pass null for skipView in recursion for children
        }
    }
}
}
