package com.ferdifir.menitcom.ui.news

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ferdifir.menitcom.R
import com.ferdifir.menitcom.data.source.Resource
import com.ferdifir.menitcom.data.ui.SectionsPagerAdapter
import com.ferdifir.menitcom.data.ui.ViewModelFactory
import com.ferdifir.menitcom.data.ui.WorldNewsAdapter
import com.ferdifir.menitcom.data.utils.Const
import com.ferdifir.menitcom.databinding.FragmentHomeBinding
import com.ferdifir.menitcom.ui.detail.DetailActivity
import com.ferdifir.menitcom.ui.explore.ExploreActivity
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var newsAdapter: WorldNewsAdapter
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireActivity())
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        configTabLayout()
        routeToExplore()
        setAdapter()
    }

    private fun setAdapter() {
        newsAdapter = WorldNewsAdapter()
        newsAdapter.onItemClick = { selectedNews ->
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra(Const.EXTRA_NEWS, selectedNews)
            startActivity(intent)
        }
        with(binding.rvWorldNews) {
            layoutManager = GridLayoutManager(context, 1, RecyclerView.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = newsAdapter
        }
        setNewsList(category = Const.category[2])
    }

    private fun setNewsList(country: String = "us", category: String) {
        viewModel.getTopNews(country, category).observe(viewLifecycleOwner) { news ->
            if (news != null) {
                when(news) {
                    is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
                    is Resource.Error -> binding.progressBar.visibility = View.GONE
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        newsAdapter.setData(news.data)
                    }
                }
            }
        }
    }

    private fun routeToExplore() {
        val intent = Intent(activity, ExploreActivity::class.java)
        val optionsCompat: ActivityOptionsCompat =
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                context as Activity,
                Pair(binding.searchNews, "search")
            )
        binding.searchNews.setOnClickListener {
            intent.putExtra(Const.SEARCH_FOKUS, true)
            startActivity(intent, optionsCompat.toBundle())
        }
        binding.tvSeaAll.setOnClickListener {
            intent.putExtra(Const.SEARCH_FOKUS, false)
            startActivity(intent, optionsCompat.toBundle())
        }
    }

    private fun configTabLayout() {
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
            R.string.tab_text_1,
            R.string.tab_text_2,
            R.string.tab_text_3,
            R.string.tab_text_4,
            R.string.tab_text_5,
            R.string.tab_text_6,
            R.string.tab_text_7
        )
    }
}