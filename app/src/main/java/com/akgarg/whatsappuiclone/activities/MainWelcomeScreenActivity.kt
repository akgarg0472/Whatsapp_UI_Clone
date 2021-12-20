package com.akgarg.whatsappuiclone.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.akgarg.whatsappuiclone.R
import com.google.firebase.auth.FirebaseAuth

class MainWelcomeScreenActivity : AppCompatActivity() {

    private lateinit var agreeAndContinueButton: Button

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        val authUser = auth.currentUser
        if (authUser != null) {
            val mainActivityIntent = Intent(this, MainActivity::class.java)
            startActivity(mainActivityIntent)
            finish()
        }

        setContentView(R.layout.activity_main_welcome_screen)
        supportActionBar?.hide()

        agreeAndContinueButton = findViewById(R.id.welcomeScreenAgreeAndContinueButton)
        agreeAndContinueButton.setOnClickListener {
            val phoneNumberActivityIntent = Intent(this, EnterPhoneNumberActivity::class.java)
            startActivity(phoneNumberActivityIntent)
            finish()
        }
    }

}