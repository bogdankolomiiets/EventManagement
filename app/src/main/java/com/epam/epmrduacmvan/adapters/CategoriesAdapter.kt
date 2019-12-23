package com.epam.epmrduacmvan.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.LayoutRes
import com.epam.epmrduacmvan.R
import com.epam.epmrduacmvan.model.Category

class CategoriesAdapter(context: Context, @LayoutRes private val layoutResource: Int, private val allCategories: List<Category>) :
    ArrayAdapter<Category>(context, layoutResource, allCategories), Filterable {
    private var listCategories: List<Category> = allCategories

    override fun getCount(): Int {
        return listCategories.size
    }

    override fun getItem(position: Int): Category? {
        return listCategories[position]
    }

    override fun getItemId(position: Int): Long {
        return listCategories[position].id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView as LinearLayout? ?: LayoutInflater.from(context).inflate(layoutResource, parent,false) as LinearLayout
        view.findViewById<TextView>(R.id.text_line_search).text = listCategories[position].name
        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
                listCategories = filterResults.values as List<Category>
                notifyDataSetChanged()
            }

            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val queryString = charSequence?.toString()?.toLowerCase()

                val filterResults = FilterResults()
                filterResults.values = if (queryString == null || queryString.isEmpty()) allCategories
                else allCategories.filter {
                    it.name.toLowerCase().contains(queryString)
                }
                return filterResults
            }
        }
    }
}