package com.ferdifir.menitcom.presentation.detail

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.webkit.*
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.ferdifir.menitcom.R
import com.ferdifir.menitcom.data.ui.ViewModelFactory
import com.ferdifir.menitcom.data.utils.Const
import com.ferdifir.menitcom.databinding.FragmentDetailBinding
import com.ferdifir.menitcom.domain.model.News

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private lateinit var viewModel: DetailViewModel
    private lateinit var detailNews: News
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory)[DetailViewModel::class.java]

        binding.swipeRefresh.setOnRefreshListener {
            binding.webView.reload()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            view.findNavController().navigateUp()
        }

        detailNews = args.detailNews
        binding.toolbarDetail.title = detailNews.sourceName

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
            binding.btnBookmark.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_bookmark))
        } else {
            binding.btnBookmark.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_bookmark_border))
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
            domStorageEnabled = true
        }
        binding.webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
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
                binding.swipeRefresh.isRefreshing = false
            }

            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest?
            ): WebResourceResponse? {
                if (request != null) {
                    if (request.url.host?.let { Regex(".*\\.ad$|.*\\.ads$").matches(it) } == true ||
                        request.url.host!!.endsWith("adserver.com") ||
                        request.url.host!!.endsWith("ads.com") ||
                        request.url.host!!.endsWith("adserver2.com") ||
                        request.url.host!!.endsWith("doubleclick.com") ||
                        request.url.host!!.endsWith("googlesyndication.com") ||
                        request.url.host!!.endsWith("iklandisini.com")
                    ) {
                        return WebResourceResponse("text/plain", "utf-8", null)
                    }
                }
                return super.shouldInterceptRequest(view, request)
            }
        }
        binding.webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        if (data != null) {
            binding.webView.loadUrl(data.url)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_detail_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.share -> {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                val body = getString(R.string.UI_share_body, detailNews.title)
                intent.putExtra(Intent.EXTRA_TEXT, body)
                startActivity(Intent.createChooser(intent, getString(R.string.UI_share_title)))
                true
            }
            R.id.copy_link -> {
                val clipboard = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText(Const.COPY_TO_CLIPBOARD, detailNews.url)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(context, getString(R.string.UI_copy_text), Toast.LENGTH_SHORT).show()
                true
            }
            R.id.open_in_browser -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(detailNews.url))
                requireActivity().startActivity(intent)
                return true
            }
            else -> false
        }
    }

}