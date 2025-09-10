package com.project.novaiptv

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.project.novaiptv.databinding.LandingPageBinding // Assuming ViewBinding
import android.widget.Button

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

    // In LandingActivity.kt

private fun hideOverlay() {
    binding.overlayContainer.visibility = View.GONE
    Log.d("FocusDebug", "---- HIDING OVERLAY ----")

    // --- Aggressively Reset Focus Properties for Main Layout and Body ---
    // 1. Make sure main layout allows descendant focus
    binding.mainConstraintLayout.descendantFocusability = ViewGroup.FOCUS_AFTER_DESCENDANTS
    Log.d("FocusDebug", "main_constraint_layout.descendantFocusability = FOCUS_AFTER_DESCENDANTS")

    // 2. Make sure body container allows descendant focus and is not focusable itself
    binding.bodyContainerLanding.descendantFocusability = ViewGroup.FOCUS_AFTER_DESCENDANTS
    binding.bodyContainerLanding.isFocusable = false
    binding.bodyContainerLanding.isFocusableInTouchMode = false
    binding.bodyContainerLanding.isEnabled = true // Ensure container is enabled
    Log.d("FocusDebug", "body_container_landing.descendantFocusability = FOCUS_AFTER_DESCENDANTS, isFocusable=false, isEnabled=true")

    // 3. Explicitly enable and make focusable the body buttons
    val bodyButtons = listOf(binding.largeButton1Landing, binding.largeButton2Landing, binding.largeButton3Landing)
    bodyButtons.forEach { button ->
        button.isFocusable = true
        button.isFocusableInTouchMode = true // Important for some scenarios
        button.isEnabled = true
        button.visibility = View.VISIBLE // Ensure they are visible
        val buttonIdName = try { resources.getResourceEntryName(button.id) } catch (e: Exception) { "N/A" }
        Log.d("FocusDebug", "Button $buttonIdName: isFocusable=${button.isFocusable}, isEnabled=${button.isEnabled}, visibility=${button.visibility}")
    }

    // 4. Also re-enable footer buttons explicitly (though they seem to work)
    val footerButtons = listOf(binding.footerButtonLeftLanding, binding.footerButtonRight1Landing, binding.footerButtonRight2Landing)
    footerButtons.forEach { button ->
        button.isFocusable = true
        button.isFocusableInTouchMode = true
        button.isEnabled = true
        val buttonIdName = try { resources.getResourceEntryName(button.id) } catch (e: Exception) { "N/A" }
        Log.d("FocusDebug", "Footer Button $buttonIdName: isFocusable=${button.isFocusable}, isEnabled=${button.isEnabled}")
    }
    
    // 5. Restore focus
    // Try focusing the first large button directly.
    val requestFocusSuccess = binding.largeButton1Landing.requestApplyInsets() // FOCUS_UP) // Or just .requestFocus()
    Log.d("FocusDebug", "Requested focus on large_button_1_landing. Success: $requestFocusSuccess")
    if (!requestFocusSuccess) {
        Log.d("FocusDebug", "Still couldn't focus large_button_1_landing. Trying a footer button as fallback.")
        binding.footerButtonRight2Landing.requestFocus() // Fallback to a known working button
    }

    lastFocusedViewBeforeOverlay = null
}

// Keep showOverlay similar, ensuring it disables things
private fun showOverlay() {
    Log.d("FocusDebug", "---- SHOWING OVERLAY ----")
    // Consider blocking focus on the main layout
    // binding.mainConstraintLayout.descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS

    // Disable body buttons explicitly
    val bodyButtons = listOf(binding.largeButton1Landing, binding.largeButton2Landing, binding.largeButton3Landing)
    bodyButtons.forEach { button ->
        button.isFocusable = false
        button.isEnabled = false
    }
    // Disable footer buttons explicitly
    val footerButtons = listOf(binding.footerButtonLeftLanding, binding.footerButtonRight1Landing, binding.footerButtonRight2Landing)
    footerButtons.forEach { button ->
        button.isFocusable = false
        button.isEnabled = false
    }

    binding.overlayContainer.visibility = View.VISIBLE
    binding.overlayContainer.isFocusable = true // Ensure overlay itself can be focused initially
    binding.overlayContainer.isEnabled = true
    binding.overlayCloseButton.requestFocus()
    Log.d("FocusDebug", "Overlay visible. Requested focus on overlay_close_button.")
}

// Remove setChildrenFocusable for now to simplify, as we are doing it explicitly above
// override fun onBackPressed()...
// onCreate()...

