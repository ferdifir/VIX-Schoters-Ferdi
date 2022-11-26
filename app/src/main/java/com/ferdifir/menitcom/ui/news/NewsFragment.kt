package com.ferdifir.menitcom.ui.news

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ferdifir.menitcom.data.source.Resource
import com.ferdifir.menitcom.data.ui.NewsSliderAdapter
import com.ferdifir.menitcom.data.ui.ViewModelFactory
import com.ferdifir.menitcom.data.utils.Const
import com.ferdifir.menitcom.databinding.FragmentNewsBinding
import com.ferdifir.menitcom.domain.model.News
import java.util.*


class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private lateinit var newsSliderAdapter: NewsSliderAdapter
    private var currentPage = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireActivity())
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
        val categoryIndex = arguments?.getInt(Const.SECTION_NUMBER, 0)
        setNewsList(category = Const.category[categoryIndex!!])
    }

    private fun setNewsList(country: String = "id", category: String) {
        viewModel.getTopNews(country, category).observe(viewLifecycleOwner) { news ->
            if (news != null) {
                when(news) {
                    is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
                    is Resource.Error -> binding.progressBar.visibility = View.GONE
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        newsSliderAdapter = NewsSliderAdapter(
                            requireContext(),
                            news.data as ArrayList<News>
                        )
                        binding.rvLatestNews.adapter = newsSliderAdapter
                        binding.indicator.setViewPager(binding.rvLatestNews)
                        val timer = Timer()
                        timer.schedule(object : TimerTask() {
                            override fun run() {
                                Handler(Looper.getMainLooper()).post {
                                    if (currentPage == news.data.size - 1) {
                                        currentPage = 0
                                    }
                                    binding.rvLatestNews.setCurrentItem(currentPage++, true)
                                }
                            }
                        }, DELAY_MS, PERIOD_MS)
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