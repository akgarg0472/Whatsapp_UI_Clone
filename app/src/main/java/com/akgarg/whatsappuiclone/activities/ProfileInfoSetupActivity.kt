package com.akgarg.whatsappuiclone.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.akgarg.whatsappuiclone.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class ProfileInfoSetupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var nameInput: EditText
    private lateinit var nextButton: Button
    private lateinit var progressBar: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_info_setup)
        supportActionBar?.hide()
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        nameInput = findViewById(R.id.profileInfoNameInput)
        nextButton = findViewById(R.id.profileInfoNextButton)
        progressBar = findViewById(R.id.profileInfoProgressBar)
        nameInput.text = Editable.Factory.getInstance().newEditable(currentUser?.displayName)

        nextButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            nextButton.isEnabled = false

            val name = nameInput.text.toString()

            when {
                name.trim().isEmpty() -> {
                    Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.INVISIBLE
                    nextButton.isEnabled = true
                }
                currentUser == null -> {
                    progressBar.visibility = View.INVISIBLE
                    nextButton.isEnabled = true

                    AlertDialog.Builder(this).setTitle("Error")
                        .setMessage("Something serious F***d up. Please verify phone again")
                        .setCancelable(false)
                        .setPositiveButton("OK") { _, _ ->
                            progressBar.visibility = View.VISIBLE
                            nextButton.isEnabled = false
                            auth.signOut()
                            startActivity(Intent(this, MainWelcomeScreenActivity::class.java))
                            finish()
                        }.show()
                }
                else -> {
                    progressBar.visibility = View.VISIBLE
                    nextButton.isEnabled = false

                    val updateProfileRequest = UserProfileChangeRequest
                        .Builder().setDisplayName(name)
                        .build()
                    currentUser.updateProfile(updateProfileRequest).addOnCompleteListener {
                        if (it.isSuccessful) {
                            progressBar.visibility = View.INVISIBLE
                            nextButton.isEnabled = true
                            val mainActivityIntent = Intent(this, MainActivity::class.java)
                            startActivity(mainActivityIntent)
                            finish()
                        } else {
                            progressBar.visibility = View.INVISIBLE
                            nextButton.isEnabled = true
                            println(it.exception)
                            println(it.exception?.message)
                            Toast.makeText(this, "Something wrong happened", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }
    }

}