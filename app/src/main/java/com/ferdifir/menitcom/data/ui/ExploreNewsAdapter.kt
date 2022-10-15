package com.ferdifir.menitcom.data.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ferdifir.menitcom.R
import com.ferdifir.menitcom.data.utils.Helper
import com.ferdifir.menitcom.databinding.ItemListExploreBinding
import com.ferdifir.menitcom.domain.model.News

class ExploreNewsAdapter: RecyclerView.Adapter<ExploreNewsAdapter.ListViewHolder>() {

    private var listData = ArrayList<News>()
    var onItemClick: ((News) -> Unit)? = null

    fun setData(newListData: List<News>?) {
        if (newListData == null)
            return listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = ItemListExploreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    inner class ListViewHolder(private val binding: ItemListExploreBinding):
        RecyclerView.ViewHolder(binding.root) {
            fun bind(data: News) {
                val date = Helper.convertDate(data.publishedAt.toString())
                val desc = "$date - ${data.description}"
                with(binding) {
                    if (data.url!!.length > 36)
                        tvListUrl.text = itemView.context.getString(R.string.UI_triple_dot, data.url.take(33))

                    if (data.title.length > 77)
                        tvListTitle.text = itemView.context.getString(R.string.UI_triple_dot, data.title.take(74))
                    else
                        tvListTitle.text = data.title

                    if (desc.length > 135)
                        tvListDesc.text = itemView.context.getString(R.string.UI_triple_dot, desc.take(132))
                    else
                        tvListDesc.text = desc
                }
            }
        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(listData[adapterPosition])
            }
        }
    }
}