package com.akgarg.whatsappuiclone.activities

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setMargins
import com.akgarg.whatsappuiclone.R
import com.akgarg.whatsappuiclone.constants.SharedPreferenceConstants
import com.akgarg.whatsappuiclone.utils.SharedPreferenceUtil
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity : AppCompatActivity() {

    private lateinit var settingsPageProfileName: TextView
    private lateinit var settingsPageProfilePicture: ImageView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Settings"
        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(
                resources.getColor(
                    R.color.tab_layout_bg_color,
                    theme
                )
            )
        )
        auth = FirebaseAuth.getInstance()

        settingsPageProfileName = findViewById(R.id.settingsPageProfileName)
        settingsPageProfilePicture = findViewById(R.id.settingsPageProfilePicture)

        val username: String? = SharedPreferenceUtil.getStringPreference(
            this,
            SharedPreferenceConstants.REGISTERED_USER_NAME
        )
        settingsPageProfileName.text = getString(R.string.registered_user_name, username.toString())
        updateSettingPageProfilePicture()
    }


    private fun updateSettingPageProfilePicture() {
        settingsPageProfilePicture.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        settingsPageProfilePicture.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        settingsPageProfilePicture.scaleType = ImageView.ScaleType.FIT_XY
        settingsPageProfilePicture.imageTintMode = null
        val layoutParams = FrameLayout.LayoutParams(settingsPageProfilePicture.layoutParams)
        layoutParams.setMargins(0)
        settingsPageProfilePicture.layoutParams = layoutParams
        Glide.with(this).load(auth.currentUser?.photoUrl).into(settingsPageProfilePicture)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }

        return true
    }

}