package com.akgarg.whatsappuiclone.activities

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.akgarg.whatsappuiclone.R
import com.akgarg.whatsappuiclone.constants.SharedPreferenceConstants
import com.akgarg.whatsappuiclone.utils.SharedPreferenceUtil

class SettingsActivity : AppCompatActivity() {

    private lateinit var settingsPageProfileName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Settings"
        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(
                resources.getColor(
                    R.color.tab_layout_bg_color,
                    theme
                )
            )
        )

        settingsPageProfileName = findViewById(R.id.settingsPageProfileName)
        val username: String? = SharedPreferenceUtil.getStringPreference(
            this,
            SharedPreferenceConstants.REGISTERED_USER_NAME
        )
        settingsPageProfileName.text = getString(R.string.registered_user_name, username.toString())
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }

        return true
    }

}