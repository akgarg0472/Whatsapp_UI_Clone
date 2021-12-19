package com.akgarg.whatsappuiclone.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.akgarg.whatsappuiclone.R
import com.akgarg.whatsappuiclone.utils.OtpUtils

@Suppress("ControlFlowWithEmptyBody")
class VerifyNumberOtpActivity : AppCompatActivity() {

    private lateinit var otpInputs: ArrayList<EditText>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_number_otp)
        supportActionBar?.hide()

        otpInputs = ArrayList()
        otpInputs.add(findViewById(R.id.otpET1))
        otpInputs.add(findViewById(R.id.otpET2))
        otpInputs.add(findViewById(R.id.otpET3))
        otpInputs.add(findViewById(R.id.otpET4))
        otpInputs.add(findViewById(R.id.otpET5))
        otpInputs.add(findViewById(R.id.otpET6))

        otpInputs.forEachIndexed { index, editText ->
            editText.addTextChangedListener(object : TextWatcher {

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    println("$p1 $p2 $p3")

                    if (editText.text.toString().length == 1 && index < otpInputs.size - 1) {
                        editText.clearFocus()
                        otpInputs[index + 1].requestFocus()
                        otpInputs[index + 1].isCursorVisible = true
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (index == otpInputs.size - 1) {
                        verifyOtpProcess()
                    }

                    if (editText.text.toString().isEmpty() && index > 0) {
                        editText.clearFocus()
                        otpInputs[index - 1].requestFocus()
                        otpInputs[index - 1].isCursorVisible = true
                    }
                }
            })
        }

    }

    private fun verifyOtpProcess() {
        val otp = OtpUtils.fetchInputOtp(otpInputs)
        val isOtpValid: Boolean = otp.length == otpInputs.size

        if (isOtpValid) {
            // perform validation
        }
    }

}