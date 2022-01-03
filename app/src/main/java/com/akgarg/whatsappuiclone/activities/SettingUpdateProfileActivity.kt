package com.akgarg.whatsappuiclone.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.akgarg.whatsappuiclone.R
import com.akgarg.whatsappuiclone.constants.ApplicationConstants
import com.akgarg.whatsappuiclone.constants.ApplicationLoggingConstants
import com.akgarg.whatsappuiclone.constants.SharedPreferenceConstants
import com.akgarg.whatsappuiclone.utils.SharedPreferenceUtil
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest

class SettingUpdateProfileActivity : AppCompatActivity() {

    private lateinit var settingUpdateProfileName: TextView
    private lateinit var settingUpdateProfilePhone: TextView
    private lateinit var settingUpdateProfileAbout: TextView
    private lateinit var settingProfileUpdateImageView: ImageView
    private lateinit var updateProfileUpdateNameRelativeLayout: RelativeLayout
    private lateinit var auth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_update_profile)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Profile"
        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(
                resources.getColor(
                    R.color.tab_layout_bg_color,
                    theme
                )
            )
        )
        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser!!

        settingUpdateProfileName = findViewById(R.id.settingUpdateProfileName)
        settingUpdateProfilePhone = findViewById(R.id.settingUpdateProfilePhone)
        settingUpdateProfileAbout = findViewById(R.id.settingUpdateProfileAbout)
        settingProfileUpdateImageView = findViewById(R.id.settingProfileUpdateImageView)
        updateProfileUpdateNameRelativeLayout =
            findViewById(R.id.updateProfileUpdateNameRelativeLayout)
        settingProfileUpdateImageView.setOnClickListener { profilePictureClickHandler() }

        settingUpdateProfileName.text = SharedPreferenceUtil.getStringPreference(
            this,
            SharedPreferenceConstants.REGISTERED_USER_NAME
        )

        settingUpdateProfilePhone.text = getString(
            R.string.profile_update_phone,
            SharedPreferenceUtil.getStringPreference(
                this,
                SharedPreferenceConstants.REGISTERED_COUNTRY_CODE
            ),
            SharedPreferenceUtil.getStringPreference(
                this,
                SharedPreferenceConstants.REGISTERED_PHONE_NUMBER
            )
        )
        settingUpdateProfileAbout.text = "Hey there, I'm using Your App"
        Glide.with(this).load(currentUser.photoUrl).into(settingProfileUpdateImageView)
        updateProfileUpdateNameRelativeLayout.setOnClickListener { updateNameClickHandler() }
    }


    @SuppressLint("InflateParams")
    private fun updateNameClickHandler() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Enter your name")
        val view = layoutInflater.inflate(R.layout.update_name_input_layout, null)
        val editText: EditText = view.findViewById(R.id.newNameInputEditText)
        editText.setText(
            SharedPreferenceUtil.getStringPreference(
                this,
                SharedPreferenceConstants.REGISTERED_USER_NAME
            )
        )
        editText.requestFocus()
        editText.selectAll()
        alertDialogBuilder.setView(view)

        alertDialogBuilder.setPositiveButton("Save") { dialog, _ ->
            val newName = editText.text.toString()
            if (newName.isEmpty()) {
                Toast.makeText(this, "Name can't be empty", Toast.LENGTH_SHORT).show()
            } else {
                updateUserName(newName)
                dialog.dismiss()
            }
        }

        alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.setOnShowListener {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(Color.parseColor("#4DAC9C"))
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(Color.parseColor("#4DAC9C"))
        }

        alertDialog.window?.setGravity(Gravity.BOTTOM)
        alertDialog.show()
    }


    private fun profilePictureClickHandler() {
        val fullScreenProfilePictureIntent = Intent(this, FullScreenProfilePicture::class.java)
        fullScreenProfilePictureIntent.putExtra(
            ApplicationConstants.FULL_SCREEN_PROFILE_PICTURE_TITLE,
            "Profile Photo"
        )
        fullScreenProfilePictureIntent.putExtra(
            ApplicationConstants.FULL_SCREEN_PROFILE_PICTURE_URL,
            currentUser.photoUrl.toString()
        )
        startActivity(fullScreenProfilePictureIntent)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }

        return true
    }


    private fun updateUserName(newName: String) {
        val profileUpdateRequest = UserProfileChangeRequest
            .Builder()
            .setDisplayName(newName)

        currentUser.updateProfile(profileUpdateRequest.build()).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "Name Updated Successfully", Toast.LENGTH_SHORT).show()
                Log.d(
                    ApplicationLoggingConstants.PROFILE_NAME_CHANGED.toString(),
                    "Profile Name Changed"
                )
                SharedPreferenceUtil.setStringPreference(
                    this,
                    SharedPreferenceConstants.REGISTERED_USER_NAME,
                    newName
                )
                settingUpdateProfileName.text = SharedPreferenceUtil.getStringPreference(
                    this,
                    SharedPreferenceConstants.REGISTERED_USER_NAME
                )
            }
        }
    }

}