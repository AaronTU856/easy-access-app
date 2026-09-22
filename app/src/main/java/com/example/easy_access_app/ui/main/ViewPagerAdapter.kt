package com.example.easy_access_app.ui.main

import android.view.MotionEvent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.easy_access_app.ui.card.CardFragment
import com.example.easy_access_app.ui.home.HomeFragment
import com.example.easy_access_app.ui.library.LibraryFragment
import com.example.easy_access_app.ui.printer.PrinterFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 4 // Number of fragments

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> CardFragment()
            2 -> LibraryFragment()
            3 -> PrinterFragment()
            else -> throw IllegalStateException("Invalid position")
        }
    }
}

