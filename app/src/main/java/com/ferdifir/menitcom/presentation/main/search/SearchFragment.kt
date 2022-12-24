package com.ferdifir.menitcom.presentation.main.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ferdifir.menitcom.R
import com.ferdifir.menitcom.data.ui.SectionsPagerAdapter
import com.ferdifir.menitcom.databinding.FragmentSearchBinding
import com.google.android.material.tabs.TabLayoutMediator

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTabLayout()

        binding.cardview.setOnClickListener {
            binding.appBarLayout.setExpanded(false, false)
            showSearchView(true)
            binding.etSearch.requestFocus()
        }

        binding.btnBack.setOnClickListener {
            showSearchView(false)
            binding.appBarLayout.setExpanded(true, true)
        }

        binding.btnFilter.setOnClickListener {

        }

        binding.appBarLayout.addOnOffsetChangedListener { _, verticalOffset ->
            if (verticalOffset == 0) {
                showSearchView(false)
            }
        }
    }

    private fun showSearchView(state: Boolean) {
        if (state) {
            binding.tabs.visibility = View.GONE
            binding.searchLayout.visibility = View.VISIBLE
            binding.viewPager.visibility = View.GONE
        } else {
            binding.tabs.visibility = View.VISIBLE
            binding.searchLayout.visibility = View.GONE
            binding.viewPager.visibility = View.VISIBLE
        }
    }

    private fun setupTabLayout() {
        val sectionsPagerAdapter = SectionsPagerAdapter(activity as AppCompatActivity)
        binding.viewPager.adapter = sectionsPagerAdapter
        binding.viewPager.isUserInputEnabled = false
        TabLayoutMediator(binding.tabs, binding.viewPager) { tabs, position ->
            tabs.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_3,
            R.string.tab_text_1,
            R.string.tab_text_2,
            R.string.tab_text_4,
            R.string.tab_text_5,
            R.string.tab_text_6,
            R.string.tab_text_7
        )
    }
}