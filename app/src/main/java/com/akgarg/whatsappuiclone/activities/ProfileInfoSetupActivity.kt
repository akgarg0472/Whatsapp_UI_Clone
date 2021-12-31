package com.akgarg.whatsappuiclone.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.akgarg.whatsappuiclone.R
import com.akgarg.whatsappuiclone.constants.ApplicationLoggingConstants
import com.akgarg.whatsappuiclone.constants.SharedPreferenceConstants
import com.akgarg.whatsappuiclone.utils.SharedPreferenceUtil
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ProfileInfoSetupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var nameInput: EditText
    private lateinit var nextButton: Button
    private lateinit var progressBar: RelativeLayout
    private lateinit var profilePictureSelector: CardView
    private lateinit var profilePicture: ImageView
    private lateinit var imageChooserAction: ActivityResultLauncher<Intent>
    private lateinit var storageRef: StorageReference
    private var currentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_info_setup)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        storageRef = FirebaseStorage.getInstance().reference
        currentUser = auth.currentUser
        var profilePictureUri: Uri? = null

        nameInput = findViewById(R.id.profileInfoNameInput)
        nextButton = findViewById(R.id.profileInfoNextButton)
        progressBar = findViewById(R.id.profileInfoProgressBar)

        if (currentUser != null && currentUser?.displayName != null) {
            nameInput.text = Editable.Factory.getInstance().newEditable(currentUser?.displayName)
        }

        profilePictureSelector = findViewById(R.id.profilePictureSelectorProfileInfo)
        profilePicture = findViewById(R.id.addPhotoProfileInfoSetup)

        imageChooserAction = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK && currentUser != null) {
                profilePicture.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                profilePicture.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                profilePicture.scaleType = ImageView.ScaleType.FIT_XY
                profilePicture.imageTintMode = null
                profilePictureUri = it.data?.data
                Glide.with(this).load(profilePictureUri).into(profilePicture)
            }
        }

        profilePictureSelector.setOnClickListener {
            profilePictureSelectorClickHandler()
        }

        nextButton.setOnClickListener {
            nextButtonClickHandler(profilePictureUri)
        }
    }


    private fun profilePictureSelectorClickHandler() {
        val getIntent = Intent(Intent.ACTION_GET_CONTENT)
        getIntent.type = "image/*"
        val pickIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = "image/*"
        val chooserIntent = Intent.createChooser(getIntent, "Select Profile Picture")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))
        imageChooserAction.launch(chooserIntent)
    }


    private fun nextButtonClickHandler(profilePictureUri: Uri?) {
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
                updateProfileAndProceed(name, profilePictureUri)
            }
        }
    }

    private fun updateProfileAndProceed(name: String, profilePictureUri: Uri?) {
        progressBar.visibility = View.VISIBLE
        nextButton.isEnabled = false

        if (currentUser != null) {
            val updateProfileRequestBuilder = UserProfileChangeRequest
                .Builder().setDisplayName(name)

            if (profilePictureUri != null) {
                Log.d(
                    ApplicationLoggingConstants.FIREBASE_IMAGE_TAG.toString(),
                    profilePictureUri.toString()
                )

                Log.d(
                    ApplicationLoggingConstants.FIREBASE_IMAGE_TAG.toString(),
                    "Starting executing upload picture method"
                )

                val image = storageRef.child("profile_pictures/${currentUser?.uid}")
                image.putFile(profilePictureUri)
                    .addOnSuccessListener {
                        val uploadSessionUri = it.uploadSessionUri.toString()
                        val str1 =
                            uploadSessionUri.substring(0, uploadSessionUri.indexOf("&uploadType"))
                        val finalUrl = "$str1&alt=media"
                        Log.d(ApplicationLoggingConstants.FIREBASE_IMAGE_TAG.toString(), finalUrl)
                        updateProfileRequestBuilder.photoUri = Uri.parse(finalUrl)
                        updateFirebaseProfile(currentUser!!, updateProfileRequestBuilder, name)
                    }
                    .addOnFailureListener {
                        Log.d(
                            ApplicationLoggingConstants.FIREBASE_IMAGE_TAG.toString(),
                            "Error uploading image file. ${it.message}"
                        )
                        updateFirebaseProfile(currentUser!!, updateProfileRequestBuilder, name)
                    }
            }
        } else {
            Toast.makeText(
                this,
                "Application Error, Please reinstall application or try clearing app data",
                Toast.LENGTH_LONG
            ).show()
        }
    }


    private fun updateFirebaseProfile(
        currentUser: FirebaseUser,
        updateProfileRequestBuilder: UserProfileChangeRequest.Builder,
        name: String
    ) {
        currentUser.updateProfile(updateProfileRequestBuilder.build())
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    progressBar.visibility = View.INVISIBLE
                    nextButton.isEnabled = true
                    SharedPreferenceUtil.setStringPreference(
                        this,
                        SharedPreferenceConstants.REGISTERED_USER_NAME,
                        name
                    )
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