package com.ferdifir.menitcom.ui.bookmark

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ferdifir.menitcom.data.ui.ViewModelFactory
import com.ferdifir.menitcom.data.ui.WorldNewsAdapter
import com.ferdifir.menitcom.data.utils.Const
import com.ferdifir.menitcom.databinding.FragmentBookmarkBinding
import com.ferdifir.menitcom.ui.detail.DetailActivity
import com.ferdifir.menitcom.ui.news.HomeViewModel

class BookmarkFragment : Fragment() {

    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: BookmarkViewModel
    private lateinit var newsAdapter: WorldNewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireActivity())
        viewModel = ViewModelProvider(this, factory)[BookmarkViewModel::class.java]

        setAdapter()
    }

    private fun setAdapter() {
        newsAdapter = WorldNewsAdapter()
        newsAdapter.onItemClick = { selectedNews ->
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra(Const.EXTRA_NEWS, selectedNews)
            startActivity(intent)
        }
        with(binding.rvBookmarkNews) {
            layoutManager = GridLayoutManager(context, 2, RecyclerView.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = newsAdapter
        }
        viewModel.bookMarkedNews.observe(viewLifecycleOwner) { bookmarkedNews ->
            if (bookmarkedNews != null) {
                binding.notFound.visibility = View.GONE
                binding.notFoundText.visibility = View.GONE
                newsAdapter.setData(bookmarkedNews)
            } else {
                binding.notFound.visibility = View.VISIBLE
                binding.notFoundText.visibility = View.VISIBLE
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}