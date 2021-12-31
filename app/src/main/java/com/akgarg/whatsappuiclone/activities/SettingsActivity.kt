package com.akgarg.whatsappuiclone.activities

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
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
    private lateinit var settingsPagePersonalInformationContainer: RelativeLayout
    private lateinit var settingsPageQrCodeContainer: RelativeLayout
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
        settingsPagePersonalInformationContainer =
            findViewById(R.id.settingsPagePersonalInformationContainer)
        settingsPageQrCodeContainer = findViewById(R.id.settingsPageQrCodeContainer)

        val username: String? = SharedPreferenceUtil.getStringPreference(
            this,
            SharedPreferenceConstants.REGISTERED_USER_NAME
        )

        settingsPageQrCodeContainer.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Please Confirm")
                .setMessage("Wanna use this feature?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, _ ->
                    dialog.dismiss()
                    Toast.makeText(
                        this,
                        "Please share your bank information to +91 9643454500 to use this feature!!!",
                        Toast.LENGTH_LONG
                    ).show()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                    Toast.makeText(this, "As Your wish 😒😒", Toast.LENGTH_SHORT).show()
                }
                .show()
        }

        settingsPagePersonalInformationContainer.setOnClickListener {
            val updateProfileActivityIntent = Intent(this, SettingUpdateProfileActivity::class.java)
            startActivity(updateProfileActivityIntent)
        }

        settingsPageProfileName.text = getString(R.string.registered_user_name, username.toString())
    }


    override fun onResume() {
        super.onResume()
        updateSettingPageProfilePicture()
        settingsPageProfileName.text = getString(
            R.string.registered_user_name,
            SharedPreferenceUtil.getStringPreference(
                this,
                SharedPreferenceConstants.REGISTERED_USER_NAME
            )
        )
    }


    private fun updateSettingPageProfilePicture() {
        settingsPageProfilePicture.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        settingsPageProfilePicture.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
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