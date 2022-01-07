package com.akgarg.whatsappuiclone.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.akgarg.whatsappuiclone.R
import com.akgarg.whatsappuiclone.constants.ApplicationConstants
import com.akgarg.whatsappuiclone.constants.ApplicationLoggingConstants
import com.akgarg.whatsappuiclone.constants.FirebaseConstants
import com.akgarg.whatsappuiclone.constants.SharedPreferenceConstants
import com.akgarg.whatsappuiclone.models.firebase.User
import com.akgarg.whatsappuiclone.utils.SharedPreferenceUtil
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import java.net.UnknownHostException

class SettingUpdateProfileActivity : AppCompatActivity() {

    private lateinit var settingUpdateProfileName: TextView
    private lateinit var settingUpdateProfilePhone: TextView
    private lateinit var settingUpdateProfileAbout: TextView
    private lateinit var settingProfileUpdateImageView: ImageView
    private lateinit var updateProfileUpdateNameRelativeLayout: RelativeLayout
    private lateinit var updateProfilePictureProgressBar: RelativeLayout
    private lateinit var updateProfilePictureButton: FloatingActionButton
    private lateinit var auth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser
    private lateinit var imageChooserAction: ActivityResultLauncher<Intent>


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
        updateProfilePictureButton = findViewById(R.id.newProfilePictureSettingFab)
        updateProfileUpdateNameRelativeLayout =
            findViewById(R.id.updateProfileUpdateNameRelativeLayout)
        updateProfilePictureProgressBar = findViewById(R.id.updateProfilePictureProgressBar)

        settingProfileUpdateImageView.setOnClickListener { profilePictureClickHandler() }

        updateProfileUpdateNameRelativeLayout.setOnClickListener { updateNameClickHandler() }

        updateProfilePictureButton.setOnClickListener { updateProfilePictureClickHandler() }

        imageChooserAction = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            println("UpdateProfilePictureActivityResultCode: ${it.resultCode}")

            if (it.resultCode == Activity.RESULT_OK) {
                settingProfileUpdateImageView.layoutParams.width =
                    ViewGroup.LayoutParams.MATCH_PARENT
                settingProfileUpdateImageView.layoutParams.height =
                    ViewGroup.LayoutParams.MATCH_PARENT
                settingProfileUpdateImageView.scaleType = ImageView.ScaleType.CENTER_CROP
                settingProfileUpdateImageView.imageTintMode = null
                val profilePictureUri = it.data?.data

                try {
                    updateCurrentUserProfilePictureInDatabase(profilePictureUri)
                } catch (e: Exception) {
                    profilePictureUpdateFailed()
                }
            }
        }
    }


    private fun updateCurrentUserProfilePictureInDatabase(profilePictureUri: Uri?) {
        if (profilePictureUri != null) {
            updateProfilePictureProgressBar.visibility = View.VISIBLE

            val storage = FirebaseStorage.getInstance()
            storage.maxUploadRetryTimeMillis = 18000
            val profilePictureRef =
                storage.reference.child("${FirebaseConstants.PROFILE_PICTURE_DIRECTORY_PATH}/${currentUser.uid}")

            profilePictureRef
                .putFile(profilePictureUri)
                .addOnFailureListener {
                    Log.d(
                        ApplicationLoggingConstants.FIREBASE_PROFILE_PICTURE_UPLOAD_FAIL.toString(),
                        "Error uploading updated profile picture. \nCause: ${it.message}"
                    )

                    if (it.javaClass == StorageException::javaClass ||
                        it.javaClass == FirebaseNetworkException::javaClass
                        || it.javaClass == UnknownHostException::javaClass
                    ) {
                        Toast.makeText(
                            this,
                            "Error Connecting to server",
                            Toast.LENGTH_SHORT
                        ).show()
                        updateProfilePictureProgressBar.visibility = View.INVISIBLE
                    } else {
                        profilePictureUpdateFailed()
                    }
                }
                .addOnCompleteListener { imageUploadTask ->
                    if (imageUploadTask.isSuccessful) {
                        val imageUploadTaskSnapshot = imageUploadTask.result
                        val uploadSessionUri = imageUploadTaskSnapshot.uploadSessionUri.toString()
                        val str1 =
                            uploadSessionUri.substring(0, uploadSessionUri.indexOf("&uploadType"))
                        val newProfilePictureUrl = "$str1&alt=media"
                        val profileUpdateRequest =
                            UserProfileChangeRequest.Builder()
                                .setPhotoUri(Uri.parse(newProfilePictureUrl))

                        currentUser
                            .updateProfile(profileUpdateRequest.build())
                            .addOnCompleteListener { currentUserUpdateTask ->
                                if (currentUserUpdateTask.isSuccessful) {
                                    val usersCollection = FirebaseFirestore.getInstance()
                                        .collection(FirebaseConstants.FIREBASE_USERS_COLLECTION)

                                    usersCollection
                                        .document(currentUser.uid)
                                        .get()
                                        .addOnSuccessListener { userSnapshot ->
                                            val user = userSnapshot.toObject<User>()

                                            if (user != null) {
                                                usersCollection.document(user.getUid())
                                                    .update(
                                                        "profilePictureUrl",
                                                        newProfilePictureUrl
                                                    )
                                                    .addOnCompleteListener { userProfilePictureUpdateTask ->
                                                        if (userProfilePictureUpdateTask.isSuccessful) {
                                                            Toast.makeText(
                                                                this,
                                                                "Profile Picture updated successfully",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                            updateProfilePictureProgressBar.visibility =
                                                                View.INVISIBLE
                                                            updateProfilePictureImageContent()
                                                        } else {
                                                            profilePictureUpdateFailed()
                                                        }
                                                    }
                                                    .addOnFailureListener {
                                                        profilePictureUpdateFailed()
                                                    }
                                                    .addOnCanceledListener {
                                                        profilePictureUpdateFailed()
                                                    }
                                            } else {
                                                profilePictureUpdateFailed()
                                            }
                                        }
                                        .addOnFailureListener {
                                            profilePictureUpdateFailed()
                                        }
                                        .addOnCanceledListener {
                                            profilePictureUpdateFailed()
                                        }
                                } else {
                                    profilePictureUpdateFailed()
                                }
                            }
                            .addOnFailureListener {
                                profilePictureUpdateFailed()
                            }
                            .addOnCanceledListener {
                                profilePictureUpdateFailed()
                            }
                    } else if (imageUploadTask.exception != null) {
                        Log.d("DisplayPicture", imageUploadTask.exception?.message.toString())
                        profilePictureUpdateFailed()
                    }
                }
        }
    }


    private fun profilePictureUpdateFailed() {
        updateProfilePictureProgressBar.visibility = View.INVISIBLE
        Toast.makeText(
            this,
            "Error updating profile picture",
            Toast.LENGTH_SHORT
        ).show()
    }


    override fun onStart() {
        super.onStart()
        settingUpdateProfileName.text = SharedPreferenceUtil.getStringPreference(
            this,
            SharedPreferenceConstants.REGISTERED_USER_NAME
        )

        Log.d("DP", "onStart() called")

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
        settingUpdateProfileAbout.text =
            SharedPreferenceUtil.getStringPreference(this, SharedPreferenceConstants.PROFILE_STATUS)

        updateProfilePictureImageContent()
    }


    private fun updateProfilePictureImageContent() {
        if (currentUser.photoUrl != null && currentUser.photoUrl.toString() != "null") {
            println(currentUser.photoUrl)
            settingProfileUpdateImageView.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            settingProfileUpdateImageView.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            settingProfileUpdateImageView.scaleType = ImageView.ScaleType.CENTER_CROP
            settingProfileUpdateImageView.imageTintMode = null
            Glide.with(this).load(currentUser.photoUrl).into(settingProfileUpdateImageView)
//            settingProfileUpdateImageView.setImageURI(currentUser.photoUrl)
        }
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
                if (currentUser.displayName != newName.trim()) {
                    updateUserName(newName.trim())
                }
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
        val profilePicture = currentUser.photoUrl.toString()

        if (profilePicture != "null") {
            val fullScreenProfilePictureIntent = Intent(this, FullScreenProfilePicture::class.java)
            fullScreenProfilePictureIntent.putExtra(
                ApplicationConstants.FULL_SCREEN_PROFILE_PICTURE_TITLE,
                "Profile Photo"
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
                    "Profile Name updated"
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

                FirebaseFirestore.getInstance()
                    .collection(FirebaseConstants.FIREBASE_USERS_COLLECTION)
                    .document(currentUser.uid)
                    .update("name", newName)
            }
        }
    }


    private fun updateProfilePictureClickHandler() {
        val getIntent = Intent(Intent.ACTION_GET_CONTENT)
        getIntent.type = "image/*"
        val pickIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = "image/*"
        val chooserIntent = Intent.createChooser(getIntent, "Select Profile Picture")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))
        imageChooserAction.launch(chooserIntent)
    }


}