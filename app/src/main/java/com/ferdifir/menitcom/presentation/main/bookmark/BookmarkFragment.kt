package com.ferdifir.menitcom.presentation.main.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ferdifir.menitcom.data.ui.BookmarkedNewsAdapter
import com.ferdifir.menitcom.data.ui.ViewModelFactory
import com.ferdifir.menitcom.databinding.FragmentBookmarkBinding

class BookmarkFragment : Fragment() {

    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!
    private lateinit var newsAdapter: BookmarkedNewsAdapter
    private lateinit var bookmarkViewModel: BookmarkViewModel

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
        bookmarkViewModel = ViewModelProvider(this, factory)[BookmarkViewModel::class.java]
        setBookmarkList()
    }

    private fun setBookmarkList() {
        newsAdapter = BookmarkedNewsAdapter()
        bookmarkViewModel.bookMarkedNews.observe(viewLifecycleOwner) { newsList ->
            if (newsList != null) {
                binding.ivNoData.visibility = View.GONE
                binding.tvNoData.visibility = View.GONE
                binding.rvBookmark.visibility = View.VISIBLE
                newsAdapter.onItemClick = { selectedNews ->
                    val action = BookmarkFragmentDirections.actionNavBookmarkToNavDetail(selectedNews)
                    requireView().findNavController().navigate(action)
                }
                newsAdapter.setData(newsList)
                with(binding.rvBookmark) {
                    layoutManager = GridLayoutManager(
                        context,
                        2,
                        RecyclerView.HORIZONTAL,
                        false
                    )
                    setHasFixedSize(true)
                    adapter = newsAdapter
                }
            } else {
                binding.ivNoData.visibility = View.VISIBLE
                binding.tvNoData.visibility = View.VISIBLE
                binding.rvBookmark.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}