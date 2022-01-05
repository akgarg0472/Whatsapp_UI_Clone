package com.akgarg.whatsappuiclone.activities

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.akgarg.whatsappuiclone.R

class LinkedDevicesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_linked_devices)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Linked devices"
        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(
                resources.getColor(
                    R.color.tab_layout_bg_color,
                    theme
                )
            )
        )
    }

}