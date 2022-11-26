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
import com.ferdifir.menitcom.data.utils.Helper
import com.ferdifir.menitcom.domain.model.News
import com.ferdifir.menitcom.ui.detail.DetailActivity

class NewsSliderAdapter(
    private val context: Context,
    private var data: ArrayList<News>
): PagerAdapter() {

    override fun getCount(): Int = 10

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View =
            (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                R.layout.item_list_latest,
                container,
                false
            )

        val ivImages = view.findViewById<ImageView>(R.id.iv_item_image)
        val tvTitle = view.findViewById<TextView>(R.id.tv_item_title)
        val tvDateTime = view.findViewById<TextView>(R.id.tv_source_and_date)
        val tvDesc = view.findViewById<TextView>(R.id.tv_item_desc)

        view.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(Const.EXTRA_NEWS, data[position])
            context.startActivity(intent)
        }
        data[position].let { news ->
            val date = Helper.convertDate(news.publishedAt.toString())
            val sourceAndDate = "${news.sourceName} - on $date"
            Glide.with(context)
                .load(news.urlToImage)
                .into(ivImages)
            tvTitle.text = news.title
            tvDateTime.text = sourceAndDate
            tvDesc.text = news.description
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