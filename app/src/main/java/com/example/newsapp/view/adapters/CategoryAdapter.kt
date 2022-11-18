package com.example.newsapp.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.util.Variables.Companion.SELECTED_CATEGORY
import com.example.newsapp.view.fragments.BreakingNewsFragment
import java.util.*


class CategoryAdapter(
    private val fragment : Fragment
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>(){

    private val differCallback = object : DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    val categoryDiffer = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_item_preview, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.category.text = categoryDiffer.currentList[position]
        holder.itemView.setOnClickListener{
            SELECTED_CATEGORY = holder.category.text.toString().lowercase(Locale.ROOT)
            Toast.makeText(fragment.context, SELECTED_CATEGORY, Toast.LENGTH_SHORT).show()
            if (fragment is BreakingNewsFragment){
                fragment.loadCategoryNews()
            }
        }
    }

    override fun getItemCount(): Int {
        return categoryDiffer.currentList.size
    }

    inner class CategoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val category : TextView = itemView.findViewById(R.id.tvCategory)
    }
}