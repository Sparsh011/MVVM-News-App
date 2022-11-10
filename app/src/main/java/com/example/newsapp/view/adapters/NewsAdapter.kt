package com.example.newsapp.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.model.api.Article
import com.example.newsapp.view.fragments.BreakingNewsFragment
import com.example.newsapp.view.fragments.SavedNewsFragment
import com.example.newsapp.view.fragments.SearchNewsFragment

class NewsAdapter(
    private val fragment: Fragment
): RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_article_preview, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.tvSource.text = article.source?.name
        holder.tvDescription.text = article.description
        holder.tvTitle.text = article.title
        holder.tvPublishedAt.text = article.publishedAt

        Glide.with(fragment)
            .load(article.urlToImage)
            .into(holder.ivArticleImage)


        holder.itemView.setOnClickListener{
            if (fragment is BreakingNewsFragment){
                fragment.articleWebView(article)
            }
            else if (fragment is SearchNewsFragment){
                fragment.articleWebView(article)
            }
            else if (fragment is SavedNewsFragment){
                fragment.articleWebView(article)
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class ArticleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tvSource: TextView = itemView.findViewById(R.id.tvSource)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val tvPublishedAt: TextView = itemView.findViewById(R.id.tvPublishedAt)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val ivArticleImage : ImageView = itemView.findViewById(R.id.ivArticleImage)
    }

//    var onItemClickListener: ((Article) -> Unit)? = null
//
//    fun setOnItemClickListener(listener: (Article) -> Unit){
//        onItemClickListener = listener
//    }
}