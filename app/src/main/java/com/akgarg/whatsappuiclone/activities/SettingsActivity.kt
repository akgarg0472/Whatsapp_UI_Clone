package com.akgarg.whatsappuiclone.activities

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setMargins
import com.akgarg.whatsappuiclone.R
import com.akgarg.whatsappuiclone.constants.SharedPreferenceConstants
import com.akgarg.whatsappuiclone.utils.SharedPreferenceUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity : AppCompatActivity() {

    private lateinit var profileName: TextView
    private lateinit var settingPageStatus: TextView
    private lateinit var profilePicture: ImageView
    private lateinit var personalInformationContainer: RelativeLayout
    private lateinit var qrCodeContainer: RelativeLayout
    private lateinit var mainSettingAccountContainer: RelativeLayout
    private lateinit var mainSettingChatContainer: RelativeLayout
    private lateinit var helpContainer: RelativeLayout
    private lateinit var mainSettingInviteFriendContainer: RelativeLayout
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

        profileName = findViewById(R.id.settingsPageProfileName)
        settingPageStatus = findViewById(R.id.settingsPageStatus)
        profilePicture = findViewById(R.id.settingsPageProfilePicture)
        personalInformationContainer = findViewById(R.id.settingsPagePersonalInformationContainer)
        qrCodeContainer = findViewById(R.id.settingsPageQrCodeContainer)
        mainSettingAccountContainer = findViewById(R.id.mainSettingAccountContainer)
        mainSettingChatContainer = findViewById(R.id.mainSettingChatContainer)
        mainSettingInviteFriendContainer = findViewById(R.id.mainSettingInviteFriendContainer)
        helpContainer = findViewById(R.id.mainSettingHelpContainer)

        qrCodeContainer.setOnClickListener {
            qrCodeClickHandler()
        }

        personalInformationContainer.setOnClickListener {
            startActivity(Intent(this, SettingUpdateProfileActivity::class.java))
        }

        mainSettingAccountContainer.setOnClickListener {
            startActivity(Intent(this, AccountSettingActivity::class.java))
        }

        mainSettingChatContainer.setOnClickListener {
            startActivity(Intent(this, ChatSettingActivity::class.java))
        }

        mainSettingInviteFriendContainer.setOnClickListener {
            inviteFriendClickHandler()
        }

        helpContainer.setOnClickListener {
            startActivity(Intent(this, HelpSettingActivity::class.java))
        }

    }


    private fun inviteFriendClickHandler() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.putExtra(
            Intent.EXTRA_TEXT,
            "Hey, Please checkout this awesome WhatsApp Clone"
        )
        shareIntent.type = "text/plain"
        val chooserIntent = Intent.createChooser(shareIntent, "Share App using..")
        startActivity(chooserIntent)
    }


    private fun qrCodeClickHandler() {
        Toast.makeText(
            this,
            "This feature is only available for Premium Users!!",
            Toast.LENGTH_LONG
        ).show()
    }


    override fun onResume() {
        super.onResume()
        updateSettingPageProfilePicture()
        profileName.text = getString(
            R.string.registered_user_name,
            SharedPreferenceUtil.getStringPreference(
                this,
                SharedPreferenceConstants.REGISTERED_USER_NAME
            )
        )
        settingPageStatus.text =
            SharedPreferenceUtil.getStringPreference(this, SharedPreferenceConstants.PROFILE_STATUS)
    }


    private fun updateSettingPageProfilePicture() {
        val userProfilePicture = auth.currentUser?.photoUrl.toString()

        if (userProfilePicture != "null") {
            profilePicture.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            profilePicture.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            profilePicture.imageTintMode = null
            val layoutParams = FrameLayout.LayoutParams(profilePicture.layoutParams)
            layoutParams.setMargins(0)
            profilePicture.layoutParams = layoutParams

            Glide.with(this)
                .load(userProfilePicture)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return true
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }
                })
                .into(profilePicture)
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }

        return true
    }

}