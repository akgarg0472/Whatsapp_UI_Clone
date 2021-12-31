package com.akgarg.whatsappuiclone.utils

import android.net.Uri
import android.util.Log
import com.akgarg.whatsappuiclone.constants.ApplicationLoggingConstants
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage

class FirebaseUtils {

    companion object {

        fun uploadPicture(profilePictureUri: Uri, currentUser: FirebaseUser): Uri? {
            val storageRef = FirebaseStorage.getInstance().reference
            var uploadedImageUri: Uri? = null
            Log.d(ApplicationLoggingConstants.FIREBASE_IMAGE_TAG.toString(), "Starting executing upload picture method")

            val image = storageRef.child("profile_pictures/${currentUser.uid}")
            image.putFile(profilePictureUri)
                .addOnSuccessListener {
                    val uploadSessionUri = it.uploadSessionUri.toString()
                    val str1 =
                        uploadSessionUri.substring(0, uploadSessionUri.indexOf("&uploadType"))
                    val finalUrl = "$str1&alt=media"
                    Log.d(ApplicationLoggingConstants.FIREBASE_IMAGE_TAG.toString(), finalUrl)
                    uploadedImageUri = Uri.parse(finalUrl)
                }
                .addOnFailureListener {
                    Log.d(
                        ApplicationLoggingConstants.FIREBASE_IMAGE_TAG.toString(),
                        "Error uploading image file. ${it.message}"
                    )
                }

            return uploadedImageUri
        }
    }
}