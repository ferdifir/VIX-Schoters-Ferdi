package com.ferdifir.menitcom.data.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ferdifir.menitcom.databinding.ItemListBreakingNewsBinding
import com.ferdifir.menitcom.databinding.ItemListNewsBinding
import com.ferdifir.menitcom.domain.model.News

class WorldNewsAdapter: RecyclerView.Adapter<WorldNewsAdapter.ListViewHolder>() {

    private var listData = ArrayList<News>()
    var onItemClick: ((News) -> Unit)? = null

    fun setData(newListData: List<News>?) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = ItemListBreakingNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    inner class ListViewHolder(private val binding: ItemListBreakingNewsBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: News) {
            with(binding) {
                tvTitleNews.text = data.title
                Glide.with(itemView.context)
                    .load(data.urlToImage)
                    .into(ivNews)
            }
        }
        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(listData[adapterPosition])
            }
        }
    }
}