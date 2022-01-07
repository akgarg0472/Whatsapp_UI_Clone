package com.akgarg.whatsappuiclone.activities

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
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
import androidx.core.view.setMargins
import com.akgarg.whatsappuiclone.R
import com.akgarg.whatsappuiclone.constants.ApplicationConstants
import com.akgarg.whatsappuiclone.constants.ApplicationLoggingConstants
import com.akgarg.whatsappuiclone.constants.FirebaseConstants
import com.akgarg.whatsappuiclone.constants.SharedPreferenceConstants
import com.akgarg.whatsappuiclone.models.firebase.User
import com.akgarg.whatsappuiclone.utils.SharedPreferenceUtil
import com.akgarg.whatsappuiclone.utils.TimeUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
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
    private lateinit var addPhotoProfileInfoSetupProgressBar: ProgressBar
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
        addPhotoProfileInfoSetupProgressBar = findViewById(R.id.addPhotoProfileInfoSetupProgressBar)

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


    override fun onResume() {
        super.onResume()

        if (currentUser != null && currentUser?.displayName != null) {
            nameInput.text = Editable.Factory.getInstance().newEditable(currentUser?.displayName)
        }

        if (currentUser != null && currentUser?.photoUrl != null) {
            addPhotoProfileInfoSetupProgressBar.visibility = View.VISIBLE
            profilePicture.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            profilePicture.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            profilePicture.imageTintMode = null
            profilePicture.scaleType = ImageView.ScaleType.CENTER_CROP
            val layoutParams = FrameLayout.LayoutParams(profilePicture.layoutParams)
            layoutParams.setMargins(0)
            profilePicture.layoutParams = layoutParams

            Glide.with(this)
                .load(currentUser?.photoUrl)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        addPhotoProfileInfoSetupProgressBar.visibility = View.INVISIBLE
                        return true
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        addPhotoProfileInfoSetupProgressBar.visibility = View.INVISIBLE
                        return false
                    }
                })
                .into(profilePicture)
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
        profilePictureSelector.isEnabled = false
        val name = nameInput.text.toString()

        when {
            name.trim().isEmpty() -> {
                Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.INVISIBLE
                nextButton.isEnabled = true
                profilePictureSelector.isEnabled = true
            }
            currentUser == null -> {
                progressBar.visibility = View.INVISIBLE
                nextButton.isEnabled = true
                profilePictureSelector.isEnabled = true

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
        profilePictureSelector.isEnabled = false

        val updateProfileRequestBuilder = UserProfileChangeRequest
            .Builder().setDisplayName(name)

        if (currentUser != null) {
            if (profilePictureUri != null) {
                val image =
                    storageRef.child("${FirebaseConstants.PROFILE_PICTURE_DIRECTORY_PATH}/${currentUser?.uid}")
                image.putFile(profilePictureUri)
                    .addOnSuccessListener {
                        val uploadSessionUri = it.uploadSessionUri.toString()
                        val str1 =
                            uploadSessionUri.substring(0, uploadSessionUri.indexOf("&uploadType"))
                        val finalUrl = "$str1&alt=media"
                        updateProfileRequestBuilder.photoUri = Uri.parse(finalUrl)
                        updateFirebaseProfile(currentUser!!, updateProfileRequestBuilder, name)
                    }
                    .addOnFailureListener {
                        Log.d(
                            ApplicationLoggingConstants.FIREBASE_IMAGE.toString(),
                            "Error uploading image file. ${it.message}"
                        )
                        updateFirebaseProfile(currentUser!!, updateProfileRequestBuilder, name)
                    }
            } else {
                Log.d(
                    ApplicationLoggingConstants.FIREBASE_IMAGE.toString(),
                    "No Profile Picture Selected"
                )
                updateFirebaseProfile(currentUser!!, updateProfileRequestBuilder, name)
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
                    SharedPreferenceUtil.setStringPreference(
                        this,
                        SharedPreferenceConstants.REGISTERED_USER_NAME,
                        name
                    )
                    saveOrUpdateUsersCollection(currentUser)
                } else {
                    progressBar.visibility = View.INVISIBLE
                    nextButton.isEnabled = true
                    profilePictureSelector.isEnabled = true
                    println(it.exception)
                    println(it.exception?.message)
                    Toast.makeText(this, "Something wrong happened", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }


    private fun saveOrUpdateUsersCollection(currentUser: FirebaseUser?) {
        val usersCollection = FirebaseFirestore.getInstance()
            .collection(FirebaseConstants.FIREBASE_USERS_COLLECTION)

        if (currentUser != null) {
            usersCollection.document(currentUser.uid).get()
                .addOnSuccessListener {
                    if (it.data != null) {
                        val user = it.toObject<User>()
                        if (user != null) {
                            SharedPreferenceUtil.setStringPreference(
                                this,
                                SharedPreferenceConstants.REGISTERED_USER_UID,
                                currentUser.uid
                            )
                            SharedPreferenceUtil.setBooleanPreference(
                                this,
                                SharedPreferenceConstants.REGISTERED_USER_IS_READ_RECEIPT_ENABLED,
                                user.getIsReadReceiptEnabled()
                            )
                            SharedPreferenceUtil.setStringPreference(
                                this,
                                SharedPreferenceConstants.PROFILE_STATUS,
                                user.getProfileStatus()
                            )
                            updateCurrentUserInformation(
                                currentUser.uid,
                                currentUser.displayName,
                                currentUser.photoUrl,
                                user
                            )
                        }
                    } else {
                        SharedPreferenceUtil.setStringPreference(
                            this,
                            SharedPreferenceConstants.PROFILE_STATUS,
                            ApplicationConstants.DEFAULT_USER_PROFILE_STATUS
                        )
                        addCurrentUserToCollection(
                            currentUser.uid,
                            currentUser.displayName,
                            currentUser.photoUrl
                        )
                    }
                }
                .addOnFailureListener {
                    println("Error searching user.")
                    launchMainActivity()
                }
        }
    }


    private fun addCurrentUserToCollection(id: String, name: String?, photoUrl: Uri?) {
        val usersCollection = FirebaseFirestore.getInstance()
            .collection(FirebaseConstants.FIREBASE_USERS_COLLECTION)

        val user = User(
            uid = id,
            name = name,
            profilePictureUrl = photoUrl.toString(),
            countryCode = SharedPreferenceUtil.getStringPreference(
                this,
                SharedPreferenceConstants.REGISTERED_COUNTRY_CODE
            ),
            mobileNumber = SharedPreferenceUtil.getStringPreference(
                this,
                SharedPreferenceConstants.REGISTERED_PHONE_NUMBER
            ),
            isLastSeenVisible = true,
            lastSeen = "",
            profileStatus = ApplicationConstants.DEFAULT_USER_PROFILE_STATUS,
            isOnline = true,
            statusUpdatedOn = TimeUtils.getCurrentDateAndTime(),
            profileCreatedOn = TimeUtils.getCurrentDateAndTime(),
            isReadReceiptEnabled = true
        )

        SharedPreferenceUtil.setStringPreference(
            this,
            SharedPreferenceConstants.REGISTERED_USER_UID,
            user.getUid()
        )
        SharedPreferenceUtil.setBooleanPreference(
            this,
            SharedPreferenceConstants.REGISTERED_USER_IS_READ_RECEIPT_ENABLED,
            true
        )

        usersCollection.document(user.getUid()).set(user).addOnCompleteListener {
            launchMainActivity()
        }.addOnFailureListener {
            launchMainActivity()
        }
    }


    private fun updateCurrentUserInformation(
        userId: String,
        name: String?,
        profilePicture: Uri?,
        user: User
    ) {
        val usersCollection = FirebaseFirestore.getInstance()
            .collection(FirebaseConstants.FIREBASE_USERS_COLLECTION)

        val existingUser = User(user)
        val dataUpdates = hashMapOf<String, Any>()
        var isProfileUpdated = false

        // if user has updated name on setup screen
        if (name != user.getName()) {
            existingUser.setName(name)
            dataUpdates["name"] = name.toString()
            isProfileUpdated = true
        }

        // if user has updated profile picture on setup screen
        if (profilePicture.toString() != user.getProfilePictureUrl()) {
            existingUser.setProfilePictureUrl(profilePicture.toString())
            isProfileUpdated = true
        }

        if (isProfileUpdated) {
            usersCollection
                .document(userId).update(dataUpdates)
                .addOnCompleteListener {
                    launchMainActivity()
                }
                .addOnFailureListener {
                    launchMainActivity()
                }
        } else {
            launchMainActivity()
        }
    }


    private fun launchMainActivity() {
        SharedPreferenceUtil.setBooleanPreference(
            this,
            SharedPreferenceConstants.PROFILE_INFO_SETUP_COMPLETED,
            true
        )
        progressBar.visibility = View.INVISIBLE
        nextButton.isEnabled = true
        profilePictureSelector.isEnabled = true
        val mainActivityIntent = Intent(this, MainActivity::class.java)
        startActivity(mainActivityIntent)
        finish()
    }

}