package com.akgarg.whatsappuiclone.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.akgarg.whatsappuiclone.R
import com.akgarg.whatsappuiclone.constants.ApplicationConstants
import com.akgarg.whatsappuiclone.constants.ChatConstants
import com.bumptech.glide.Glide

class ChatProfileInfo : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var backToChatButton: LinearLayout
    private lateinit var chatProfileProfileImage: ImageView

    private lateinit var chatProfileRegisteredNumber: TextView
    private lateinit var chatProfileLastSeen: TextView
    private lateinit var chatProfileName: TextView
    private lateinit var chatProfileBlockUser: TextView
    private lateinit var chatProfileReportUser: TextView

    private var userDataBundle: Bundle? = null
    private var name: String? = null
    private var profilePicture: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_profile_info)

        window.statusBarColor = resources.getColor(R.color.tab_layout_bg_color, theme)
        window.navigationBarColor = resources.getColor(R.color.chat_profile_info_bg, theme)

        toolbar = findViewById(R.id.chatProfileInfoToolbar)
        backToChatButton = findViewById(R.id.backToChat)
        chatProfileProfileImage = findViewById(R.id.chatProfileProfileImage)
        chatProfileRegisteredNumber = findViewById(R.id.chatProfileRegisteredNumber)
        chatProfileName = findViewById(R.id.chatProfileName)
        chatProfileLastSeen = findViewById(R.id.chatProfileLastSeen)
        chatProfileBlockUser = findViewById(R.id.chatProfileBlockUser)
        chatProfileReportUser = findViewById(R.id.chatProfileReportUser)

        userDataBundle = intent.extras

        backToChatButton.setOnClickListener { finish() }
        chatProfileProfileImage.setOnClickListener { chatProfileProfileImageClickHandler() }
        updateToolbar()
    }


    override fun onResume() {
        super.onResume()

        name = userDataBundle?.getString(ChatConstants.CHAT_PROFILE_NAME)
        profilePicture = userDataBundle?.getString(ChatConstants.CHAT_PROFILE_PICTURE)
        chatProfileName.text = name

        if (profilePicture != null && profilePicture != "null") {
            chatProfileProfileImage.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            chatProfileProfileImage.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            chatProfileProfileImage.scaleType = ImageView.ScaleType.FIT_XY
            chatProfileProfileImage.imageTintMode = null
            Glide.with(this).load(profilePicture)
                .into(chatProfileProfileImage)
        }

        chatProfileRegisteredNumber.text =
            getString(
                R.string.chat_profile_registered_number,
                userDataBundle?.getString(ChatConstants.CHAT_PROFILE_COUNTRY_CODE),
                userDataBundle?.getString(ChatConstants.CHAT_PROFILE_PHONE_NUMBER)
            )

        chatProfileLastSeen.text = getString(
            R.string.chat_profile_last_seen,
            userDataBundle?.getString(ChatConstants.CHAT_PROFILE_LAST_SEEN)
        )

        chatProfileBlockUser.text = getString(R.string.block_user_text, name)

        chatProfileReportUser.text = getString(R.string.report_user_text, name)
    }


    private fun chatProfileProfileImageClickHandler() {
        val userProfilePicture = userDataBundle?.getString(ChatConstants.CHAT_PROFILE_PICTURE)

        if (userProfilePicture != null && userProfilePicture != "null") {
            val fullScreenProfilePictureIntent = Intent(this, FullScreenProfilePicture::class.java)
            fullScreenProfilePictureIntent.putExtra(
                ApplicationConstants.FULL_SCREEN_PROFILE_PICTURE_TITLE,
                name
            )
            fullScreenProfilePictureIntent.putExtra(
                ApplicationConstants.FULL_SCREEN_PROFILE_PICTURE_URL,
                profilePicture
            )
            startActivity(fullScreenProfilePictureIntent)
        } else {
            Toast.makeText(this, "No profile picture", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return true
    }


    private fun updateToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayUseLogoEnabled(false)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chat_profile_info_menu, menu)
        return true
    }


}