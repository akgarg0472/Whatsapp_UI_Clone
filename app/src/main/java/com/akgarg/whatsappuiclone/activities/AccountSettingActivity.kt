package com.akgarg.whatsappuiclone.activities

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.akgarg.whatsappuiclone.R
import com.akgarg.whatsappuiclone.utils.SharedPreferenceUtil
import com.google.firebase.auth.FirebaseAuth

class AccountSettingActivity : AppCompatActivity() {

    private lateinit var logout: RelativeLayout
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_setting)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Account"
        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(
                resources.getColor(
                    R.color.tab_layout_bg_color,
                    theme
                )
            )
        )

        auth = FirebaseAuth.getInstance()
        logout = findViewById(R.id.accountSettingsPageLogoutContainer)

        logout.setOnClickListener { logoutClickHandler() }
    }


    private fun logoutClickHandler() {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Please Confirm")
            .setMessage("Are you sure want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                auth.signOut()
                ActivityCompat.finishAffinity(this)
                SharedPreferenceUtil.clearAllSharedPreferences(this)
                val mainWelcomeScreenActivityIntent =
                    Intent(this, MainWelcomeScreenActivity::class.java)
                startActivity(mainWelcomeScreenActivityIntent)
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        alertDialog.setOnShowListener {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(Color.parseColor("#4DAC9C"))
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(Color.parseColor("#4DAC9C"))
        }
        alertDialog.show()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return true
    }

}