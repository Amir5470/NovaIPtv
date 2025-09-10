package com.project.novaiptv

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button // Ensure this is imported if you do explicit 'is Button' checks, though not strictly needed if only using binding.
import androidx.appcompat.app.AppCompatActivity
import com.project.novaiptv.databinding.LandingPageBinding

class LandingActivity : AppCompatActivity() {

    private lateinit var binding: LandingPageBinding
    private var lastFocusedViewBeforeOverlay: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LandingPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("FocusDebug", "LandingActivity onCreate")

        binding.footerButtonRight2Landing.setOnClickListener {
            Log.d("FocusDebug", "Settings button (footerButtonRight2Landing) clicked")
            showOverlay()
        }

        binding.overlayCloseButton.setOnClickListener {
            Log.d("FocusDebug", "Overlay close button clicked")
            hideOverlay()
        }

        // Initially hide the overlay
        binding.overlayContainer.visibility = View.GONE
    }

    private fun showOverlay() {
        Log.d("FocusDebug", "---- SHOWING OVERLAY ----")

        // Store the currently focused view if it's part of the main layout
        val currentFocus = currentFocus
        if (currentFocus != null && currentFocus.id != View.NO_ID) {
            var parent = currentFocus.parent as? View
            var isDescendantOfMain = false
            while (parent != null) {
                if (parent.id == binding.mainConstraintLayout.id) {
                    isDescendantOfMain = true
                    break
                }
                if (parent.id == binding.overlayContainer.id) {
                    isDescendantOfMain = false // Don't save focus if it's already in overlay
                    break
                }
                parent = parent.parent as? View
            }
            if (isDescendantOfMain) {
                lastFocusedViewBeforeOverlay = currentFocus
                try {
                    Log.d("FocusDebug", "Saved focus: ${resources.getResourceEntryName(lastFocusedViewBeforeOverlay!!.id)}")
                } catch (e: Exception) {
                    Log.d("FocusDebug", "Saved focus on a view with no resource ID name.")
                }
            } else {
                 lastFocusedViewBeforeOverlay = null // Clear if not relevant
            }
        } else {
            lastFocusedViewBeforeOverlay = null // Clear if no current focus
        }


        // Disable focusability for all interactive elements behind the overlay
        setFocusabilityForMainContent(binding.mainConstraintLayout, false) // Pass the root of your main content

        binding.overlayContainer.visibility = View.VISIBLE
        // Make the overlay container itself focusable to trap focus if needed,
        // although focus should go directly to its children.
        binding.overlayContainer.isFocusable = true
        binding.overlayContainer.isEnabled = true

        // Explicitly request focus on the desired element within the overlay
        val overlayFocusSuccess = binding.overlayCloseButton.requestFocus()
        Log.d("FocusDebug", "Overlay visible. Requested focus on overlay_close_button. Success: $overlayFocusSuccess")
        if (!overlayFocusSuccess) {
            Log.e("FocusDebug", "Failed to focus on overlay_close_button!")
        }
    }

    private fun hideOverlay() {
        Log.d("FocusDebug", "---- HIDING OVERLAY ----")
        binding.overlayContainer.visibility = View.GONE
        // Make overlay container non-focusable when hidden
        binding.overlayContainer.isFocusable = false
        binding.overlayContainer.isEnabled = false

        // Restore focusability for the main layout elements
        setFocusabilityForMainContent(binding.mainConstraintLayout, true) // Pass the root of your main content

        // Restore focus to the last focused view or a default one
        if (lastFocusedViewBeforeOverlay != null &&
            lastFocusedViewBeforeOverlay!!.isFocusable &&
            lastFocusedViewBeforeOverlay!!.isEnabled && // also check if it's enabled
            lastFocusedViewBeforeOverlay!!.isShown // and actually visible on screen
        ) {
            try {
                Log.d("FocusDebug", "Attempting to restore focus to: ${resources.getResourceEntryName(lastFocusedViewBeforeOverlay!!.id)}")
            } catch (e: Exception) {
                Log.d("FocusDebug", "Attempting to restore focus to a view with no resource ID name.")
            }
            val focusRestored = lastFocusedViewBeforeOverlay!!.requestFocus()
            Log.d("FocusDebug", "Focus restoration to lastFocusedView. Success: $focusRestored")
            if (!focusRestored) {
                Log.d("FocusDebug", "Could not restore focus to lastFocusedView. Falling back.")
                fallbackFocus()
            }
        } else {
            if (lastFocusedViewBeforeOverlay == null) {
                Log.d("FocusDebug", "lastFocusedViewBeforeOverlay is null. Falling back.")
            } else {
                 try {
                     Log.d("FocusDebug", "lastFocusedViewBeforeOverlay (${resources.getResourceEntryName(lastFocusedViewBeforeOverlay!!.id)}) is not focusable/enabled/shown. Falling back. IsFocusable: ${lastFocusedViewBeforeOverlay!!.isFocusable}, IsEnabled: ${lastFocusedViewBeforeOverlay!!.isEnabled}, IsShown: ${lastFocusedViewBeforeOverlay!!.isShown}")
                 } catch (e: Exception) {
                     Log.d("FocusDebug", "lastFocusedViewBeforeOverlay (no ID) is not focusable/enabled/shown. Falling back. IsFocusable: ${lastFocusedViewBeforeOverlay!!.isFocusable}, IsEnabled: ${lastFocusedViewBeforeOverlay!!.isEnabled}, IsShown: ${lastFocusedViewBeforeOverlay!!.isShown}")
                 }
            }
            fallbackFocus()
        }
        lastFocusedViewBeforeOverlay = null // Clear after attempting to restore
    }

    /**
     * Recursively sets the focusability of all focusable views within a given ViewGroup.
     *
     * @param viewGroup The ViewGroup to traverse.
     * @param focusable True to make views focusable, false otherwise.
     */
    private fun setFocusabilityForMainContent(viewGroup: ViewGroup, focusable: Boolean) {
        for (i in 0 until viewGroup.childCount) {
            val child = viewGroup.getChildAt(i)
            // Only change focusability if it's explicitly focusable in the layout
            // or if it's a type that is typically focusable (e.g., Button).
            // You might want to refine this condition based on your specific UI components.
            if (child.isFocusableByDefault || child is Button) {
                 Log.d("FocusDebug", "Setting focusable $focusable for ${try {resources.getResourceEntryName(child.id)} catch (e:Exception) {"NO_ID"}}")
                child.isFocusable = focusable
                child.isEnabled = focusable // Often you want to disable them too
            }
            if (child is ViewGroup) {
                // If the child is itself a ViewGroup (and not the overlay container), recurse
                if (child.id != binding.overlayContainer.id) {
                    setFocusabilityForMainContent(child, focusable)
                }
            }
        }
        // Also set focusability for the root main content view itself if needed.
        // viewGroup.isFocusable = focusable
    }

    private fun fallbackFocus() {
        Log.d("FocusDebug", "Fallback focus: attempting to focus on footerButtonRight2Landing")
        // Attempt to focus a known default element in your main layout
        val fallbackSuccess = binding.footerButtonRight2Landing.requestFocus()
        if (!fallbackSuccess) {
            Log.e("FocusDebug", "Fallback focus on footerButtonRight2Landing FAILED.")
            // As a last resort, clear focus from the current view if any
            currentFocus?.clearFocus()
        }
    }
}
