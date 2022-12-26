package com.ferdifir.menitcom.presentation.main.news

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
                            /*val intent = Intent(activity, DetailActivity::class.java)
                            intent.putExtra(Const.EXTRA_NEWS, selectedNews)
                            startActivity(intent)*/
                            /*val action = NewsFragmentDirections.actionNavNewsToNavDetail(selectedNews)
                            requireView().findNavController().navigate(action)*/
                            /*val navHostFragment = childFragmentManager.findFragmentById(R.id.nav_host_fragment_home) as NavHostFragment
                            val navController = navHostFragment.findNavController()*/
                            //navController.navigate(NewsFragmentDirections.actionNavNewsToNavDetail(selectedNews))
                        }
                        newsAdapter.setData(news.data)
                        with(binding.rvLatestNews) {
                            layoutManager = LinearLayoutManager(context)
                            setHasFixedSize(true)
                            adapter = newsAdapter
                        }
                    }
                }
            }
        }
    }

}