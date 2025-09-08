package com.project.novaiptv

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.project.novaiptv.databinding.LandingPageBinding // Import ViewBinding class

class LandingActivity : AppCompatActivity() {

    private lateinit var binding: LandingPageBinding // Declare binding variable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout using ViewBinding
        binding = LandingPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Optional: If you want to use the Toolbar from your layout as the ActionBar
        // setSupportActionBar(binding.toolbarLanding)

        // Set up click listener to show the overlay
        binding.largeButton1Landing.setOnClickListener {
            binding.overlayContainer.visibility = View.VISIBLE
        }

        // Set up click listener for the overlay's close button
        binding.overlayCloseButton.setOnClickListener {
            binding.overlayContainer.visibility = View.GONE
        }
    }
}