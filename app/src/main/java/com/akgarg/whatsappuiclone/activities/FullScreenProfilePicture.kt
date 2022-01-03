package com.akgarg.whatsappuiclone.activities

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.akgarg.whatsappuiclone.R
import com.akgarg.whatsappuiclone.constants.ApplicationConstants
import com.bumptech.glide.Glide

class FullScreenProfilePicture : AppCompatActivity() {

    private lateinit var fullScreenProfilePictureImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_profile_picture)

        fullScreenProfilePictureImageView = findViewById(R.id.fullScreenProfilePictureImageView)

        val title = intent.getStringExtra(ApplicationConstants.FULL_SCREEN_PROFILE_PICTURE_TITLE)
        val profilePictureUrlString =
            intent.getStringExtra(ApplicationConstants.FULL_SCREEN_PROFILE_PICTURE_URL)
        val profilePictureUrlInt =
            intent.getIntExtra(ApplicationConstants.FULL_SCREEN_PROFILE_PICTURE_URL, 0)

        updateActionBar(title)

        if (profilePictureUrlString != null) {
            updateProfilePictureImage(profilePictureUrlString)
        } else {
            updateProfilePictureImage(profilePictureUrlInt)
        }
    }


    private fun updateProfilePictureImage(profilePictureUrl: Any?) {
        Glide.with(this).load(profilePictureUrl)
            .into(fullScreenProfilePictureImageView)
    }


    private fun updateActionBar(title: String?) {
        supportActionBar?.title = title

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(
                resources.getColor(
                    R.color.black,
                    theme
                )
            )
        )
        window.statusBarColor = Color.BLACK
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else {
            Toast.makeText(this, "Not Available Right Now", Toast.LENGTH_SHORT).show()
        }

        return true
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.full_profile_picture_menu, menu)
        return true
    }


}