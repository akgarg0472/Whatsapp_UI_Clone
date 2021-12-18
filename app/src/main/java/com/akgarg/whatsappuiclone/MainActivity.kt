package com.akgarg.whatsappuiclone

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.akgarg.whatsappuiclone.adapters.FragmentViewPagerAdapter
import com.google.android.material.tabs.TabLayout


class MainActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var viewPagerAdapter: FragmentViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.tab_layout_bg_color)))

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
    }


    private fun setCameraTab() {
        val layout =
            (tabLayout.getChildAt(0) as LinearLayout).getChildAt(0) as LinearLayout
        val layoutParams = layout.layoutParams as LinearLayout.LayoutParams
        layoutParams.weight = 0.41f
        layout.layoutParams = layoutParams

        tabLayout.getTabAt(0)?.setIcon(R.drawable.ic_baseline_photo_camera_24)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

}