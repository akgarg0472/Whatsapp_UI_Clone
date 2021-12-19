package com.akgarg.whatsappuiclone.activities

import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.akgarg.whatsappuiclone.R

class EnterPhoneNumberActivity : AppCompatActivity() {

    private lateinit var countrySelectorSpinner: Spinner
    private lateinit var countryCodeTextView: EditText
    private lateinit var nextButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_phone_number)
        supportActionBar?.hide()

        countrySelectorSpinner = findViewById(R.id.countryCode_picker_enter_phone)
        countryCodeTextView = findViewById(R.id.enterPhoneCountryCode)
        nextButton = findViewById(R.id.enterNumberNextButton)

        ArrayAdapter.createFromResource(
            this,
            R.array.countries,
            android.R.layout.simple_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            countrySelectorSpinner.adapter = adapter
        }

        countrySelectorSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    when (p2) {
                        0 -> {
                            countryCodeTextView.text =
                                Editable.Factory.getInstance()
                                    .newEditable(getString(R.string.enter_phone_country_code, 91))
                        }
                        1 -> {
                            countryCodeTextView.text =
                                Editable.Factory.getInstance()
                                    .newEditable(getString(R.string.enter_phone_country_code, 971))
                        }
                        2 -> {
                            countryCodeTextView.text = Editable.Factory.getInstance()
                                .newEditable(getString(R.string.enter_phone_country_code, 1))
                        }
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }
    }

}