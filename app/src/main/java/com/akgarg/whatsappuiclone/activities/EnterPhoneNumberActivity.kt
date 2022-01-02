package com.akgarg.whatsappuiclone.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.akgarg.whatsappuiclone.R
import com.akgarg.whatsappuiclone.constants.AuthenticationConstants
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class EnterPhoneNumberActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var countrySelectorSpinner: Spinner
    private lateinit var countryCode: EditText
    private lateinit var phoneNumber: EditText
    private lateinit var nextButton: Button
    private lateinit var progressBar: RelativeLayout

    private lateinit var auth: FirebaseAuth
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_phone_number)
        supportActionBar?.hide()
        auth = FirebaseAuth.getInstance()

        countrySelectorSpinner = findViewById(R.id.countryCode_picker_enter_phone)
        countryCode = findViewById(R.id.enterPhoneCountryCode)
        phoneNumber = findViewById(R.id.enterNumberInput)
        nextButton = findViewById(R.id.enterNumberNextButton)
        progressBar = findViewById(R.id.enterPhoneNumberProgressBar)

        val mobileNumber: String? =
            intent.getStringExtra(AuthenticationConstants.PHONE_NUMBER.toString())

        if (mobileNumber != null) {
            phoneNumber.text = Editable.Factory.getInstance().newEditable(mobileNumber)
        }

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                progressBar.visibility = View.INVISIBLE
                nextButton.isEnabled = true
                Log.d("SPECIAL_CASE", "CALLING BABE")
                val mainActivityIntent = Intent(applicationContext, MainActivity::class.java)
                startActivity(mainActivityIntent)
                finish()
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Log.d("FirebaseAuthenticationError", p0.message.toString())
                progressBar.visibility = View.INVISIBLE
                nextButton.isEnabled = true

                if (p0 is FirebaseNetworkException) {
                    Toast.makeText(
                        this@EnterPhoneNumberActivity,
                        "Internet Connectivity error",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@EnterPhoneNumberActivity,
                        "Something wrong happened",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                resendToken = p1
                progressBar.visibility = View.INVISIBLE
                nextButton.isEnabled = true

                val otpVerificationActivityIntent =
                    Intent(applicationContext, VerifyNumberOtpActivity::class.java)
                otpVerificationActivityIntent.putExtra(
                    AuthenticationConstants.PHONE_NUMBER.toString(),
                    phoneNumber.text.toString()
                )
                otpVerificationActivityIntent.putExtra(
                    AuthenticationConstants.COUNTRY_CODE.toString(),
                    countryCode.text.toString().substring(1)
                )
                otpVerificationActivityIntent.putExtra(
                    AuthenticationConstants.VERIFICATION_ID.toString(),
                    p0
                )
                startActivity(otpVerificationActivityIntent)
                finish()
            }
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.countries,
            android.R.layout.simple_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            countrySelectorSpinner.adapter = adapter
        }
        countrySelectorSpinner.onItemSelectedListener = this

        nextButton.setOnClickListener {
            val number: String = phoneNumber.text.toString().trim()

            if (countryCode.text.toString() != "+91") {
                Toast.makeText(
                    this,
                    "This app currently supports only Indian numbers",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            when {
                number.isEmpty() -> {
                    val alertDialog = AlertDialog.Builder(this).setTitle("Error")
                        .setMessage("Please Enter Phone Number")
                        .setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                        }.create()
                    alertDialog.setOnShowListener {
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                            .setTextColor(resources.getColor(R.color.chat_title_text_color, theme))
                    }
                    alertDialog.show()
                }

                number.length != 10 -> {
                    val alertDialog = AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage("Phone Number entered is invalid")
                        .setPositiveButton("Ok") { dialog, _ ->
                            dialog.dismiss()
                        }.create()
                    alertDialog.setOnShowListener {
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                            .setTextColor(resources.getColor(R.color.chat_title_text_color, theme))
                    }
                    alertDialog.show()
                }

                else -> {
                    val enteredPhoneNumber =
                        getString(R.string.entered_phone_number, countryCode.text, number)
                    val alertDialog = AlertDialog.Builder(this)
                        .setMessage("You entered the phone number\n\n${enteredPhoneNumber}\n\nIs this OK, or would you like to edit the number?")
                        .setPositiveButton("OK") { _, _ ->
                            try {
                                val imm: InputMethodManager =
                                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                                imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
                            } catch (e: Exception) {
                            }
                            generateOtp()
                        }.setNegativeButton("Edit") { dialog, _ ->
                            dialog.dismiss()
                            phoneNumber.requestFocus()
                            phoneNumber.isCursorVisible = true
                        }.create()
                    alertDialog.setOnShowListener {
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                            .setTextColor(resources.getColor(R.color.chat_title_text_color, theme))
                        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                            .setTextColor(resources.getColor(R.color.chat_title_text_color, theme))
                    }
                    alertDialog.show()
                }
            }
        }
    }


    private fun generateOtp() {
        progressBar.visibility = View.VISIBLE
        nextButton.isEnabled = false

        val authOptions = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(
                "+${
                    countryCode.text.toString().substring(1)
                }${phoneNumber.text}"
            )
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(authOptions)
    }


    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when (p2) {
            0 -> {
                countryCode.text =
                    Editable.Factory.getInstance()
                        .newEditable(getString(R.string.enter_phone_country_code, 91))
            }
            1 -> {
                countryCode.text =
                    Editable.Factory.getInstance()
                        .newEditable(getString(R.string.enter_phone_country_code, 971))
            }
            2 -> {
                countryCode.text = Editable.Factory.getInstance()
                    .newEditable(getString(R.string.enter_phone_country_code, 1))
            }
        }
    }


    override fun onNothingSelected(p0: AdapterView<*>?) {}

}