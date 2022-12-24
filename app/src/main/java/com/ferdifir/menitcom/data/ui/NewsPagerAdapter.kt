package com.ferdifir.menitcom.data.ui

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.ferdifir.menitcom.R
import com.ferdifir.menitcom.data.utils.Const
import com.ferdifir.menitcom.domain.model.News
import com.ferdifir.menitcom.presentation.detail.DetailActivity

class NewsPagerAdapter(
    private val context: Context,
    private var newsList: List<News>
): PagerAdapter() {

    override fun getCount(): Int = newsList.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view === `object`

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View =
            (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                R.layout.item_slider_news,
                null
            )
        val ivImage = view.findViewById<ImageView>(R.id.iv_slide_news)
        val tvTitle = view.findViewById<TextView>(R.id.tv_slide_title)
        val btnView = view.findViewById<TextView>(R.id.learn_more)

        newsList[position].let { news ->
            tvTitle.text = news.title!!.substringBeforeLast("-")
            Glide.with(context)
                .load(news.urlToImage)
                .into(ivImage)

            btnView.setOnClickListener {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra(Const.EXTRA_NEWS, news)
                context.startActivity(intent)
            }
        }

        val vp = container as ViewPager
        vp.addView(view, 0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val vp = container as ViewPager
        val view = `object` as View
        vp.removeView(view)
    }
}