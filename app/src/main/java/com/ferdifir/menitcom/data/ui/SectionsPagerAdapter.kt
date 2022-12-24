package com.ferdifir.menitcom.data.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ferdifir.menitcom.data.utils.Const
import com.ferdifir.menitcom.presentation.main.news.NewsFragment

class SectionsPagerAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return 7
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = NewsFragment()
        fragment.arguments = Bundle().apply {
            putInt(Const.SECTION_NUMBER, position)
        }
        return fragment
    }

}