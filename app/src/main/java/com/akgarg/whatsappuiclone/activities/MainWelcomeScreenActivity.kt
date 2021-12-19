package com.akgarg.whatsappuiclone.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.akgarg.whatsappuiclone.R

class MainWelcomeScreenActivity : AppCompatActivity() {

    private lateinit var agreeAndContinueButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_welcome_screen)
        supportActionBar?.hide()

        agreeAndContinueButton = findViewById(R.id.welcomeScreenAgreeAndContinueButton)
        agreeAndContinueButton.setOnClickListener {
            val phoneNumberActivityIntent = Intent(this, EnterPhoneNumberActivity::class.java)
            finish()
            startActivity(phoneNumberActivityIntent)
        }
    }

    
}