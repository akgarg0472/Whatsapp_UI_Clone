package com.akgarg.whatsappuiclone.activities

import android.os.Bundle
import android.text.Html
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.akgarg.whatsappuiclone.BuildConfig
import com.akgarg.whatsappuiclone.R

class AppInfoActivity : AppCompatActivity() {

    private lateinit var license: TextView
    private lateinit var appVersion: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_info)

        supportActionBar?.hide()
        window.statusBarColor = resources.getColor(R.color.app_info_title_color, theme)

        license = findViewById(R.id.appInfoLicenseLink)
        appVersion = findViewById(R.id.appInfoAppVersion)
    }


    override fun onResume() {
        super.onResume()
        appVersion.text = getString(
            R.string.app_info_version,
            BuildConfig.VERSION_CODE,
            BuildConfig.VERSION_NAME
        )
        license.text = Html.fromHtml(getString(R.string.licenses), Html.FROM_HTML_MODE_LEGACY)
    }

}