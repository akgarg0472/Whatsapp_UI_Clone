package com.akgarg.whatsappuiclone.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.akgarg.whatsappuiclone.R
import com.akgarg.whatsappuiclone.constants.SharedPreferenceConstants
import com.akgarg.whatsappuiclone.utils.SharedPreferenceUtil
import com.google.firebase.auth.FirebaseAuth

class MainWelcomeScreenActivity : AppCompatActivity() {

    private lateinit var agreeAndContinueButton: Button

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        setContentView(R.layout.activity_main_welcome_screen)
        supportActionBar?.hide()

        agreeAndContinueButton = findViewById(R.id.welcomeScreenAgreeAndContinueButton)
        agreeAndContinueButton.setOnClickListener {
            val phoneNumberActivityIntent = Intent(this, EnterPhoneNumberActivity::class.java)
            startActivity(phoneNumberActivityIntent)
            finish()
        }
    }


    override fun onStart() {
        super.onStart()
        val authUser = auth.currentUser

        if (authUser != null) {
            val isProfileInfoSetupCompleted = SharedPreferenceUtil.getBooleanPreference(
                this,
                SharedPreferenceConstants.PROFILE_INFO_SETUP_COMPLETED
            )

            if (isProfileInfoSetupCompleted) {
                val mainActivityIntent = Intent(this, MainActivity::class.java)
                startActivity(mainActivityIntent)
                finish()
            } else {
                val profileInfoActivityIntent =
                    Intent(this, ProfileInfoSetupActivity::class.java)
                startActivity(profileInfoActivityIntent)
                finish()
            }
        }
    }

}