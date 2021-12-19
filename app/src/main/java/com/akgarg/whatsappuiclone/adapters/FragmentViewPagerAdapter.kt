package com.akgarg.whatsappuiclone.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.akgarg.whatsappuiclone.fragments.CallsFragment
import com.akgarg.whatsappuiclone.fragments.CameraFragment
import com.akgarg.whatsappuiclone.fragments.ChatsFragment
import com.akgarg.whatsappuiclone.fragments.StatusFragment

@Suppress("unused", "deprecation")
class FragmentViewPagerAdapter(fm: FragmentManager, behavior: Int) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val itemsCount: Int = behavior

    override fun getCount(): Int {
        return itemsCount
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> CameraFragment()
            1 -> ChatsFragment()
            2 -> StatusFragment()
            3 -> CallsFragment()
            else -> ChatsFragment()
        }
    }

}