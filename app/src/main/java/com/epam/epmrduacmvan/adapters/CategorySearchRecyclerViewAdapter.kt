package com.epam.epmrduacmvan.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.epam.epmrduacmvan.R
import com.epam.epmrduacmvan.model.Category

class CategorySearchRecyclerViewAdapter: RecyclerView.Adapter<CategorySearchRecyclerViewAdapter.CategoryViewHolder>() {
    private var category: MutableList<Category> = mutableListOf()
    private lateinit var mOnClickListener: View.OnClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)
        view.setOnClickListener(mOnClickListener)
        return CategoryViewHolder(view)
    }

    fun setOnClickListener(listener: View.OnClickListener){
        mOnClickListener = listener
    }

    override fun getItemCount(): Int {
        return category.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.categoryName.text = category[position].name
    }

    fun setData(category: List<Category>) {
        this.category.clear()
        this.category.addAll(category)
        notifyDataSetChanged()
    }

    inner class CategoryViewHolder(item: View): RecyclerView.ViewHolder(item) {
        val categoryName: TextView = item.findViewById(R.id.category_name)
    }
}