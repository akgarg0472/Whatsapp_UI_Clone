package com.akgarg.whatsappuiclone.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.akgarg.whatsappuiclone.R
import com.akgarg.whatsappuiclone.constants.AuthenticationConstants
import com.akgarg.whatsappuiclone.constants.SharedPreferenceConstants
import com.akgarg.whatsappuiclone.utils.OtpUtils
import com.akgarg.whatsappuiclone.utils.SharedPreferenceUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

@Suppress("ControlFlowWithEmptyBody")
class VerifyNumberOtpActivity : AppCompatActivity() {

    private lateinit var otpInputs: ArrayList<EditText>
    private lateinit var phoneNumber: String
    private lateinit var countryCode: String

    private lateinit var verifyNumberHeader: TextView
    private lateinit var mobileNumberUndergoingVerification: TextView
    private lateinit var wrongNumber: TextView
    private lateinit var progressBar: RelativeLayout

    private lateinit var auth: FirebaseAuth
    private lateinit var verificationId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_number_otp)
        supportActionBar?.hide()
        auth = FirebaseAuth.getInstance()
        verificationId =
            intent.getStringExtra(AuthenticationConstants.VERIFICATION_ID.toString()).toString()

        phoneNumber = intent.getStringExtra(AuthenticationConstants.PHONE_NUMBER.toString())!!
        countryCode = intent.getStringExtra(AuthenticationConstants.COUNTRY_CODE.toString())!!
        verifyNumberHeader = findViewById(R.id.verifyNumberHeaderText)
        mobileNumberUndergoingVerification = findViewById(R.id.mobileNumberUndergoingVerification)
        progressBar = findViewById(R.id.verifyNumberOtpProgressBar)
        wrongNumber = findViewById(R.id.wrongNumberTextView)

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

        wrongNumber.setOnClickListener {
            AlertDialog.Builder(this).setCancelable(false).setTitle("Confirm")
                .setMessage("Are you sure want to edit your number +$countryCode $phoneNumber")
                .setPositiveButton("Edit") { _, _ ->
                    val enterNumberActivityIntent =
                        Intent(this, EnterPhoneNumberActivity::class.java)
                    enterNumberActivityIntent.putExtra(
                        AuthenticationConstants.PHONE_NUMBER.toString(),
                        phoneNumber
                    )
                    startActivity(enterNumberActivityIntent)
                    finish()
                }.setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
        }
    }


    override fun onResume() {
        super.onResume()
        verifyNumberHeader.text =
            getString(R.string.verify_phone_header_text, countryCode, phoneNumber)
        mobileNumberUndergoingVerification.text =
            getString(R.string.mobile_number_ongoing_verification, countryCode, phoneNumber)
    }


    private fun verifyOtpProcess() {
        progressBar.visibility = View.VISIBLE

        val otp = OtpUtils.fetchInputOtp(otpInputs)
        val isOtpValid: Boolean = otp.length == otpInputs.size

        if (isOtpValid) {
            val verificationCredentials = PhoneAuthProvider.getCredential(verificationId, otp)
            signinUser(verificationCredentials)
        } else {
            Toast.makeText(this, "OTP should be of length 6", Toast.LENGTH_SHORT).show()
            progressBar.visibility = View.INVISIBLE
        }
    }


    private fun signinUser(verificationCredentials: PhoneAuthCredential) {
        auth.signInWithCredential(verificationCredentials).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                SharedPreferenceUtil.setStringPreference(
                    this,
                    SharedPreferenceConstants.REGISTERED_COUNTRY_CODE,
                    countryCode
                )
                SharedPreferenceUtil.setStringPreference(
                    this,
                    SharedPreferenceConstants.REGISTERED_PHONE_NUMBER,
                    phoneNumber
                )

                val profileInfoActivityIntent =
                    Intent(this, ProfileInfoSetupActivity::class.java)
                startActivity(profileInfoActivityIntent)
                finish()
            } else {
                progressBar.visibility = View.INVISIBLE
                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show()
                    otpInputs.forEach {
                        it.text = Editable.Factory.getInstance().newEditable("")
                        it.clearFocus()
                    }
                    otpInputs[0].requestFocus()
                    otpInputs[0].isCursorVisible = true
                } else {
                    println(task.exception?.javaClass)
                    println(task.exception?.message)
                    Toast.makeText(this, "Something wrong happened", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}