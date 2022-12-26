package com.ferdifir.menitcom.presentation.main.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ferdifir.menitcom.data.source.Resource
import com.ferdifir.menitcom.data.ui.NewsAdapter
import com.ferdifir.menitcom.data.ui.ViewModelFactory
import com.ferdifir.menitcom.data.utils.Const.category
import com.ferdifir.menitcom.databinding.FragmentSearchBinding
import com.ferdifir.menitcom.domain.model.News
import com.ferdifir.menitcom.presentation.main.news.HomeViewModel
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private lateinit var newsAdapter: NewsAdapter

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

        val factory = ViewModelFactory.getInstance(requireActivity())
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
        newsAdapter = NewsAdapter()

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
            it.findNavController().navigate(SearchFragmentDirections.actionNavSearchToNavFilter())
        }

        binding.appBarLayout.addOnOffsetChangedListener { _, verticalOffset ->
            if (verticalOffset == 0) {
                showSearchView(false)
            }
        }

        setupTabSelected().onEach { tab ->
            setNewsList(tab.position)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        setNewsList(0)
        configSearchNews()
    }

    private fun configSearchNews() {
        binding.etSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.saveQueryPreferences(query.toString())
                viewModel.getSearchNews().observe(viewLifecycleOwner) { news ->
                    setSearchNewsList(news)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
    }

    private fun setSearchNewsList(news: Resource<List<News>>?) {
        if (news != null) {
            when(news) {
                is Resource.Loading -> {
                    binding.loadingSearch.visibility = View.VISIBLE
                    binding.rvSearch.visibility = View.GONE
                }
                is Resource.Error -> binding.loadingSearch.visibility = View.GONE
                is Resource.Success -> {
                    binding.loadingSearch.visibility = View.GONE
                    binding.rvSearch.visibility = View.VISIBLE
                    newsAdapter.onItemClick = { selectedNews ->
                        val action = SearchFragmentDirections.actionNavSearchToNavDetail(selectedNews)
                        requireView().findNavController().navigate(action)
                    }
                    newsAdapter.setData(news.data)
                    with(binding.rvSearch) {
                        layoutManager = LinearLayoutManager(context)
                        setHasFixedSize(true)
                        adapter = newsAdapter
                        invalidate()
                    }
                }
            }
        }
    }

    private fun setNewsList(position: Int) {
        viewModel.getTopNews("id", category[position]).observe(viewLifecycleOwner) { news ->
            setSearchNewsList(news)
        }
    }

    private fun showSearchView(state: Boolean) {
        if (state) {
            binding.tabs.visibility = View.GONE
            binding.searchLayout.visibility = View.VISIBLE
            binding.rvSearch.visibility = View.GONE
        } else {
            binding.tabs.visibility = View.VISIBLE
            binding.searchLayout.visibility = View.GONE
            binding.rvSearch.visibility = View.VISIBLE
        }
    }

    private fun setupTabSelected(): Flow<TabLayout.Tab> {
        return callbackFlow {
            val listener = object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    trySend(tab).isSuccess
                }
                override fun onTabUnselected(tab: TabLayout.Tab) { }
                override fun onTabReselected(tab: TabLayout.Tab) {
                    trySend(tab).isSuccess
                }
            }
            binding.tabs.addOnTabSelectedListener(listener)
            awaitClose {
                binding.tabs.removeOnTabSelectedListener(listener)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}