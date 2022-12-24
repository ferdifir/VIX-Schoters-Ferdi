package com.ferdifir.menitcom.presentation.explore

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ferdifir.menitcom.data.source.Resource
import com.ferdifir.menitcom.data.ui.ExploreNewsAdapter
import com.ferdifir.menitcom.data.ui.ViewModelFactory
import com.ferdifir.menitcom.data.utils.Const
import com.ferdifir.menitcom.data.utils.Const.date
import com.ferdifir.menitcom.data.utils.Const.language
import com.ferdifir.menitcom.data.utils.Const.languageAdapter
import com.ferdifir.menitcom.data.utils.Const.sortedBy
import com.ferdifir.menitcom.data.utils.Const.sortener
import com.ferdifir.menitcom.data.utils.Helper.today
import com.ferdifir.menitcom.databinding.ActivityExploreBinding
import com.ferdifir.menitcom.presentation.detail.DetailActivity

@RequiresApi(Build.VERSION_CODES.O)
class ExploreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExploreBinding
    private lateinit var viewModel: ExploreViewModel
    private lateinit var exploreNewsAdapter: ExploreNewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExploreBinding.inflate(layoutInflater)
        setContentView(binding.root)
        overridePendingTransition(0, 0)
        binding.filter.visibility = View.GONE

        val searchState = intent.getBooleanExtra(Const.SEARCH_FOKUS, false)
        if (searchState) binding.searchNews.requestFocus()

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[ExploreViewModel::class.java]

        setAdapter()
        searchViewConfig()
        setFilterNews()
        setNewsList()
    }

    private var filter = false
    private fun setFilterNews() {
        setFilterButton(filter)
        binding.btnFilter.setOnClickListener {
            filter = !filter
            setFilterButton(filter)
        }

        binding.spinnerSort.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, sortener)
        binding.spinnerSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.saveSortPreferences(sortedBy[position])
                setNewsList()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        binding.spinnerLanguage.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, languageAdapter)
        binding.spinnerLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.saveLanguagePreferences(language[position])
                setNewsList()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        binding.spinnerPublishDate.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, date)
        binding.spinnerPublishDate.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val requiredDate = when (position) {
                    0 -> today.minusDays(0)
                    1 -> today.minusDays(7)
                    2 -> today.minusDays(30)
                    else -> today.minusDays(7)
                }.toString()
                viewModel.saveDatePreferences(requiredDate)
                setNewsList()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun setFilterButton(state: Boolean) {
        if (state)
            binding.filter.visibility = View.VISIBLE
        else
            binding.filter.visibility = View.GONE
    }

    private fun searchViewConfig() {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = binding.searchNews
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.saveQueryPreferences(query)
                setNewsList()
                searchView.clearFocus()
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    private fun setAdapter() {
        exploreNewsAdapter = ExploreNewsAdapter()
        exploreNewsAdapter.onItemClick = { selectedNews ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(Const.EXTRA_NEWS, selectedNews)
            startActivity(intent)
        }
        with(binding.rvExploreNews) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = exploreNewsAdapter
        }
    }

    private fun setNewsList() {
        viewModel.getSearchNews().observe(this) { explore ->
            if (explore != null) {
                when (explore) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.notFound.visibility = View.GONE
                        binding.notFoundText.visibility = View.GONE
                        exploreNewsAdapter.setData(null)
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.notFound.visibility = View.VISIBLE
                        binding.notFoundText.visibility = View.VISIBLE
                        exploreNewsAdapter.setData(null)
                    }
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.notFound.visibility = View.GONE
                        binding.notFoundText.visibility = View.GONE
                        exploreNewsAdapter.setData(explore.data)
                    }
                }
            }
        }
    }
}
