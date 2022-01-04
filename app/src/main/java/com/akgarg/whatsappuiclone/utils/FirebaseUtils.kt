package com.akgarg.whatsappuiclone.utils

import android.util.Log
import com.akgarg.whatsappuiclone.constants.ApplicationConstants
import com.akgarg.whatsappuiclone.constants.ApplicationLoggingConstants
import com.akgarg.whatsappuiclone.models.firebase.ChatMessage
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseUtils {

    companion object {

        fun uploadMessage(message: ChatMessage): String? {
            val messageCollection = FirebaseFirestore.getInstance()
                .collection(ApplicationConstants.FIREBASE_CHAT_MESSAGES_COLLECTION)

            messageCollection
                .add(message)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        println(it.result?.id)
                    } else {
                        Log.d(
                            ApplicationLoggingConstants.FIREBASE_CHAT_MESSAGE_UPLOAD.toString(),
                            it.exception?.message.toString()
                        )
                    }
                }.addOnFailureListener {
                    Log.d(
                        ApplicationLoggingConstants.FIREBASE_CHAT_MESSAGE_UPLOAD.toString(),
                        it.message.toString()
                    )
                }

            return null
        }
    }
}