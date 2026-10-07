package com.harbornews.news.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.harbornews.news.databinding.ItemArticleBinding
import com.harbornews.news.domain.model.Article

class ArticleAdapter(
    private val onClick: (Article) -> Unit,
) : ListAdapter<Article, ArticleAdapter.ViewHolder>(DiffCallback) {
    class ViewHolder(val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = getItem(position)
        holder.binding.title.text = article.title
        holder.binding.summary.text = article.summary
        holder.binding.root.setOnClickListener { onClick(article) }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Article, newItem: Article) = oldItem == newItem
    }
}
