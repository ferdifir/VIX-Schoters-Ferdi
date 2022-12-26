package com.ferdifir.menitcom.presentation.main.news

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ferdifir.menitcom.data.source.Resource
import com.ferdifir.menitcom.data.ui.NewsPagerAdapter
import com.ferdifir.menitcom.data.ui.ViewModelFactory
import com.ferdifir.menitcom.data.ui.WorldNewsAdapter
import com.ferdifir.menitcom.data.utils.Const
import com.ferdifir.menitcom.databinding.FragmentHomeBinding
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var newsAdapter: WorldNewsAdapter
    private lateinit var viewModel: HomeViewModel
    private lateinit var newsSliderAdapter: NewsPagerAdapter
    private var currentPage = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireActivity())
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        setupViewPager()
        setAdapter()
    }

    private fun setupViewPager() {
        viewModel.getTopNews("id", Const.category[0]).observe(viewLifecycleOwner) { news ->
            if (news != null) {
                when(news) {
                    is Resource.Loading -> {
                        binding.loading.visibility = View.VISIBLE
                        binding.viewPager.visibility = View.GONE
                        binding.loading.startShimmer()
                    }
                    is Resource.Error -> {
                        binding.loading.visibility = View.GONE
                        binding.viewPager.visibility = View.GONE
                    }
                    is Resource.Success -> {
                        binding.loading.visibility = View.GONE
                        binding.viewPager.visibility = View.VISIBLE
                        if (news.data != null) {
                            newsSliderAdapter = NewsPagerAdapter(requireContext(), news.data)
                            newsSliderAdapter.onItemClick = {
                                val action = HomeFragmentDirections.actionNavHomeToNavDetail(it)
                                requireView().findNavController().navigate(action)
                            }
                            binding.viewPager.adapter = newsSliderAdapter
                            val timer = Timer()
                            timer.schedule(object : TimerTask() {
                                override fun run() {
                                    Handler(Looper.getMainLooper()).post {
                                        if (currentPage == news.data.size - 1) {
                                            currentPage = 0
                                        }
                                        binding.viewPager.setCurrentItem(currentPage++, true)
                                    }
                                }
                            }, DELAY_MS, PERIOD_MS)
                        }
                    }
                }
            }
        }
    }

    private fun setAdapter() {
        newsAdapter = WorldNewsAdapter()
        newsAdapter.onItemClick = { selectedNews ->
            val action = HomeFragmentDirections.actionNavHomeToNavDetail(selectedNews)
            requireView().findNavController().navigate(action)
        }
        with(binding.rvBreakingNews) {
            layoutManager = GridLayoutManager(context, 1, RecyclerView.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = newsAdapter
        }
        setNewsList(category = Const.category[2])
    }

    private fun setNewsList(country: String = "id", category: String) {
        viewModel.getTopNews(country, category).observe(viewLifecycleOwner) { news ->
            if (news != null) {
                when(news) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.rvBreakingNews.visibility = View.GONE
                    }
                    is Resource.Error -> binding.progressBar.visibility = View.GONE
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.rvBreakingNews.visibility = View.VISIBLE
                        newsAdapter.setData(news.data)
                    }
                }
            }
        }
    }

    companion object {
        private const val DELAY_MS: Long = 500
        private const val PERIOD_MS: Long = 5000
    }
}