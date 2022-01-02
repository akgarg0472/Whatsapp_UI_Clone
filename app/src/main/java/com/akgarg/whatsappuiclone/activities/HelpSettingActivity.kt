package com.akgarg.whatsappuiclone.activities

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.akgarg.whatsappuiclone.R

class HelpSettingActivity : AppCompatActivity() {

    private lateinit var helpSettingsAppInfoContainer: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help_setting)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Help"
        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(
                resources.getColor(
                    R.color.tab_layout_bg_color,
                    theme
                )
            )
        )

        helpSettingsAppInfoContainer = findViewById(R.id.helpSettingsPageAppInfoContainer)
        helpSettingsAppInfoContainer.setOnClickListener {
            startActivity(Intent(this, AppInfoActivity::class.java))
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }

        return true
    }


}