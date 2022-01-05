package com.akgarg.whatsappuiclone.activities

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.viewpager.widget.ViewPager
import com.akgarg.whatsappuiclone.R
import com.akgarg.whatsappuiclone.adapters.FragmentViewPagerAdapter
import com.akgarg.whatsappuiclone.constants.SharedPreferenceConstants
import com.akgarg.whatsappuiclone.utils.SharedPreferenceUtil
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var viewPagerAdapter: FragmentViewPagerAdapter
    private lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(
                resources.getColor(
                    R.color.tab_layout_bg_color,
                    theme
                )
            )
        )
        supportActionBar?.elevation = 0F

        viewPager = this.findViewById(R.id.viewPager)
        tabLayout = this.findViewById(R.id.tabLayout)
        fab = findViewById(R.id.mainActivityFab)
        fab.setOnClickListener { chatFabClickHandler() }

        viewPagerAdapter = FragmentViewPagerAdapter(supportFragmentManager, 4)
        viewPager.adapter = viewPagerAdapter
        viewPager.setCurrentItem(1, true)
        tabLayout.setOnTabSelectedListener(this)

        // swipe on page fragment changer
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        setCameraTab()
        setAppTheme()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setting_menu_item -> {
                val settingActivityIntent = Intent(this, SettingsActivity::class.java)
                startActivity(settingActivityIntent)
            }
            R.id.linked_devices_menu_item -> {
                val linkedDevicesActivityIntent = Intent(this, LinkedDevicesActivity::class.java)
                startActivity(linkedDevicesActivityIntent)
            }
            R.id.payments_menu_item -> {
                val paymentsActivityIntent = Intent(this, PaymentActivity::class.java)
                startActivity(paymentsActivityIntent)
            }
            else -> {
                Toast.makeText(this, "Not Available Right Now", Toast.LENGTH_SHORT).show()
            }
        }

        return true
    }


    override fun onTabSelected(tab: TabLayout.Tab?) {
        viewPager.currentItem = tab?.position!!
        when (tab.position) {
            0 -> {
                fab.visibility = View.INVISIBLE
            }
            1 -> {
                fab.visibility = View.VISIBLE
                fab.setImageDrawable(
                    AppCompatResources.getDrawable(
                        this@MainActivity,
                        R.drawable.ic_baseline_chat_24
                    )
                )
                fab.setOnClickListener { chatFabClickHandler() }
            }
            2 -> {
                fab.setImageDrawable(
                    AppCompatResources.getDrawable(
                        this@MainActivity,
                        R.drawable.ic_baseline_photo_camera_24
                    )
                )
                fab.setOnClickListener { statusFabClickHandler() }
            }
            3 -> {
                fab.setImageDrawable(
                    AppCompatResources.getDrawable(
                        this@MainActivity,
                        R.drawable.ic_baseline_add_ic_call_24
                    )
                )
                fab.setOnClickListener { callFabClickListener() }
            }
        }
    }


    override fun onTabUnselected(tab: TabLayout.Tab?) {}


    override fun onTabReselected(tab: TabLayout.Tab?) {}


    private fun setCameraTab() {
        val layout =
            (tabLayout.getChildAt(0) as LinearLayout).getChildAt(0) as LinearLayout
        val layoutParams = layout.layoutParams as LinearLayout.LayoutParams
        layoutParams.weight = 0.41f
        layout.layoutParams = layoutParams

        val drawable =
            ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_photo_camera_24, theme)
        tabLayout.getTabAt(0)?.icon = drawable
        tabLayout.getTabAt(0)?.icon?.setTint(
            ResourcesCompat.getColor(
                resources,
                R.color.tab_layout_text_color,
                theme
            )
        )
    }


    private fun setAppTheme() {
        val appTheme =
            SharedPreferenceUtil.getStringPreference(this, SharedPreferenceConstants.APP_THEME)

        if (appTheme != null) {
            when (appTheme) {
                SharedPreferenceConstants.THEME_DARK -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }

                SharedPreferenceConstants.THEME_LIGHT -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }

                SharedPreferenceConstants.THEME_SYSTEM -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }
        }
    }


    private fun chatFabClickHandler() {
        val allContactsActivityIntent = Intent(this, AllContactsActivity::class.java)
        startActivity(allContactsActivityIntent)
    }


    private fun statusFabClickHandler() {
        Toast.makeText(this, "Status Fab clicked", Toast.LENGTH_SHORT).show()
    }


    private fun callFabClickListener() {
        Toast.makeText(this, "Call Fab clicked", Toast.LENGTH_SHORT).show()
    }

}