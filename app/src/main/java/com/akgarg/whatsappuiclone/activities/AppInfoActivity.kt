package com.akgarg.whatsappuiclone.activities

import android.os.Bundle
import android.text.Html
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.akgarg.whatsappuiclone.R

class AppInfoActivity : AppCompatActivity() {

    private lateinit var appInfoLicenseLink: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_info)

        supportActionBar?.hide()
        window.statusBarColor = resources.getColor(R.color.app_info_title_color, theme)

        appInfoLicenseLink = findViewById(R.id.appInfoLicenseLink)
    }


    override fun onResume() {
        super.onResume()
        appInfoLicenseLink.text =
            Html.fromHtml(getString(R.string.licenses), Html.FROM_HTML_MODE_LEGACY)
    }

}