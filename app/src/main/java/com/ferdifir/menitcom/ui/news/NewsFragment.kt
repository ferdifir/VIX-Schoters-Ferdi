package com.ferdifir.menitcom.ui.news

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ferdifir.menitcom.data.source.Resource
import com.ferdifir.menitcom.data.ui.LatestNewsAdapter
import com.ferdifir.menitcom.data.ui.ViewModelFactory
import com.ferdifir.menitcom.data.utils.Const
import com.ferdifir.menitcom.databinding.FragmentNewsBinding
import com.ferdifir.menitcom.ui.detail.DetailActivity

class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!
    private lateinit var newsAdapter: LatestNewsAdapter
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newsAdapter = LatestNewsAdapter()
        newsAdapter.onItemClick = { selectedNews ->
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra(Const.EXTRA_NEWS, selectedNews)
            startActivity(intent)
        }

        val factory = ViewModelFactory.getInstance(requireActivity())
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
        val categoryIndex = arguments?.getInt(Const.SECTION_NUMBER, 0)
        setNewsList(category = Const.category[categoryIndex!!])

        with(binding.rvLatestNews) {
            layoutManager = GridLayoutManager(context, 1, RecyclerView.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = newsAdapter
        }
    }

    private fun setNewsList(country: String = "id", category: String) {
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

}