package com.ferdifir.menitcom.data.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ferdifir.menitcom.data.utils.Helper
import com.ferdifir.menitcom.databinding.ItemListLatestBinding
import com.ferdifir.menitcom.domain.model.News

class LatestNewsAdapter: RecyclerView.Adapter<LatestNewsAdapter.ListViewHolder>() {

    private var listData = ArrayList<News>()
    var onItemClick: ((News) -> Unit)? = null

    fun setData(newListData: List<News>?) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = ItemListLatestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    inner class ListViewHolder(private val binding: ItemListLatestBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: News) {
            val date = Helper.convertDate(data.publishedAt.toString())
            val sourceAndDate = "${data.sourceName} - on $date"
            with(binding) {
                tvItemTitle.text = data.title
                tvSourceAndDate.text = sourceAndDate
                tvItemDesc.text = data.description
                Glide.with(itemView.context)
                    .load(data.urlToImage)
                    .into(ivItemImage)
            }
        }
        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(listData[adapterPosition])
            }
        }
    }
}