package com.akgarg.whatsappuiclone.utils

import android.widget.EditText

class OtpUtils {

    companion object {

        fun fetchInputOtp(otpInputs: ArrayList<EditText>): String {
            val otpBuilder = StringBuilder(otpInputs.size)

            otpInputs.forEach { editText ->
                otpBuilder.append(editText.text.toString().trim())
            }

            return otpBuilder.toString()
        }


        fun generateRandomOtp(): String {
            return (100000..999999).random().toString()
        }

    }
}