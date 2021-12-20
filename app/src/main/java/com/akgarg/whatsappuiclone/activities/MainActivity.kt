package com.akgarg.whatsappuiclone.activities

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.res.ResourcesCompat
import androidx.viewpager.widget.ViewPager
import com.akgarg.whatsappuiclone.R
import com.akgarg.whatsappuiclone.adapters.FragmentViewPagerAdapter
import com.akgarg.whatsappuiclone.enums.SharedPreferenceConstants
import com.akgarg.whatsappuiclone.utils.SharedPreferenceUtil
import com.google.android.material.tabs.TabLayout

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var viewPagerAdapter: FragmentViewPagerAdapter

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

        viewPagerAdapter = FragmentViewPagerAdapter(supportFragmentManager, 4)
        viewPager.adapter = viewPagerAdapter
        viewPager.setCurrentItem(1, true)

        tabLayout.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager.currentItem = tab?.position!!
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        // swipe on page fragment changer
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        setCameraTab()
        setAppTheme()
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


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.light_theme_menu_item -> {
                SharedPreferenceUtil.setStringPreference(
                    this,
                    SharedPreferenceConstants.APP_THEME,
                    SharedPreferenceConstants.THEME_LIGHT
                )
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            R.id.dark_theme_menu_item -> {
                SharedPreferenceUtil.setStringPreference(
                    this,
                    SharedPreferenceConstants.APP_THEME,
                    SharedPreferenceConstants.THEME_DARK
                )
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }

            R.id.system_theme_menu_item -> {
                SharedPreferenceUtil.setStringPreference(
                    this,
                    SharedPreferenceConstants.APP_THEME,
                    SharedPreferenceConstants.THEME_SYSTEM
                )
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }

            R.id.setting_menu_item -> {
                val settingActivityIntent = Intent(this, SettingsActivity::class.java)
                startActivity(settingActivityIntent)
            }

            R.id.app_theme_menu_item -> {
                super.onOptionsItemSelected(item)
            }

            else -> {
                Toast.makeText(this, "503 Unavailable", Toast.LENGTH_SHORT).show()
            }
        }

        return true
    }


    private fun setCameraTab() {
        val layout =
            (tabLayout.getChildAt(0) as LinearLayout).getChildAt(0) as LinearLayout
        val layoutParams = layout.layoutParams as LinearLayout.LayoutParams
        layoutParams.weight = 0.41f
        layout.layoutParams = layoutParams

        val drawable =
            ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_photo_camera_24, theme)
        drawable?.setTint(ResourcesCompat.getColor(resources, R.color.tab_layout_text_color, theme))
        tabLayout.getTabAt(0)?.icon = drawable
    }

}