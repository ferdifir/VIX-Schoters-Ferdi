package com.ferdifir.menitcom.presentation.detail

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.ferdifir.menitcom.R
import com.ferdifir.menitcom.data.ui.ViewModelFactory
import com.ferdifir.menitcom.data.utils.Const
import com.ferdifir.menitcom.databinding.ActivityDetailBinding
import com.ferdifir.menitcom.domain.model.News


class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailNews: News
    private lateinit var viewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        overridePendingTransition(0, 0)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[DetailViewModel::class.java]

        detailNews = intent.getParcelableExtra(Const.EXTRA_NEWS)!!

        setSupportActionBar(binding.toolbarDetail)
        supportActionBar?.title = detailNews.sourceName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarDetail.setNavigationOnClickListener { onBackPressed() }
        
        binding.swipeRefresh.setOnRefreshListener {
            binding.webView.reload()
        }

        configWebView()
        loadWebsite(detailNews)
        setBookmarkedNews(detailNews.isBookmarked)
    }

    private fun setBookmarkedNews(state: Boolean) {
        var statusBookmark = state
        setStatusBookmark(statusBookmark)
        binding.btnBookmark.setOnClickListener {
            statusBookmark = !statusBookmark
            viewModel.setBookmarkedNews(detailNews, statusBookmark)
            setStatusBookmark(statusBookmark)
        }
    }

    private fun setStatusBookmark(statusBookmark: Boolean) {
        if (statusBookmark) {
            binding.btnBookmark.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_bookmark))
        } else {
            binding.btnBookmark.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_bookmark_border))
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun configWebView() {
        binding.webView.settings.apply {
            javaScriptEnabled = true
            allowContentAccess = true
            useWideViewPort = true
            loadsImagesAutomatically = true
            cacheMode = WebSettings.LOAD_NO_CACHE
            setRenderPriority(WebSettings.RenderPriority.HIGH)
            setEnableSmoothTransition(true)
            domStorageEnabled = true
        }
    }

    private fun loadWebsite(data: News?) {
        binding.webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                binding.progress.visibility = View.VISIBLE
                binding.progress.progress = newProgress
                if (newProgress == 100) binding.progress.visibility = View.GONE
                if (newProgress == 50) binding.swipeRefresh.isRefreshing = false
                super.onProgressChanged(view, newProgress)
            }
        }
        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                view.loadUrl(request.toString())
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                binding.progress.visibility = View.GONE
            }
        }
        binding.webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        if (data != null) {
            binding.webView.loadUrl(data.url!!)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_detail_menu, menu)
        return true
    }

    @Suppress("DEPRECATION")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.share -> {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                val body = getString(R.string.UI_share_body, detailNews.title)
                intent.putExtra(Intent.EXTRA_TEXT, body)
                startActivity(Intent.createChooser(intent, getString(R.string.UI_share_title)))
                return true
            }
            R.id.copy_link -> {
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText(Const.COPY_TO_CLIPBOARD, detailNews.url)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(this, getString(R.string.UI_copy_text), Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.open_in_browser -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(detailNews.url))
                this.startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}