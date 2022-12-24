package com.ferdifir.menitcom.presentation.main.news

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ferdifir.menitcom.data.source.Resource
import com.ferdifir.menitcom.data.ui.NewsAdapter
import com.ferdifir.menitcom.data.ui.ViewModelFactory
import com.ferdifir.menitcom.data.utils.Const
import com.ferdifir.menitcom.databinding.FragmentNewsBinding
import com.ferdifir.menitcom.presentation.detail.DetailActivity


class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private lateinit var newsAdapter: NewsAdapter

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
        newsAdapter = NewsAdapter()
        viewModel.getTopNews(country, category).observe(viewLifecycleOwner) { news ->
            if (news != null) {
                when(news) {
                    is Resource.Loading -> {}//binding.progressBar.visibility = View.VISIBLE
                    is Resource.Error -> {}//binding.progressBar.visibility = View.GONE
                    is Resource.Success -> {
                        //binding.progressBar.visibility = View.GONE
                        newsAdapter.onItemClick = { selectedNews ->
                            val intent = Intent(activity, DetailActivity::class.java)
                            intent.putExtra(Const.EXTRA_NEWS, selectedNews)
                            startActivity(intent)
                        }
                        newsAdapter.setData(news.data)
                        with(binding.rvLatestNews) {
                            layoutManager = LinearLayoutManager(context)
                            setHasFixedSize(true)
                            adapter = newsAdapter
                        }

                        /*newsSliderAdapter = NewsSliderAdapter(
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
                        }, DELAY_MS, PERIOD_MS)*/
                    }
                }
            }
        }
    }

}