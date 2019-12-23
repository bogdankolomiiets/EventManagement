package com.epam.epmrduacmvan.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.LayoutRes
import com.epam.epmrduacmvan.R
import com.epam.epmrduacmvan.model.Language

class LanguageAdapter(context: Context, @LayoutRes private val layoutResource: Int, private val allLanguage: List<Language>) :
    ArrayAdapter<Language>(context, layoutResource, allLanguage), Filterable {
    private var listLanguage: List<Language> = allLanguage

    override fun getCount(): Int {
        return listLanguage.size
    }

    override fun getItem(position: Int): Language? {
        return listLanguage[position]
    }

    override fun getItemId(position: Int): Long {
        return listLanguage[position].id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView as LinearLayout? ?: LayoutInflater.from(context).inflate(layoutResource, parent,false) as LinearLayout
        view.findViewById<TextView>(R.id.text_line_search).text = listLanguage[position].name
        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
                listLanguage = filterResults.values as List<Language>
                notifyDataSetChanged()
            }

            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val queryString = charSequence?.toString()?.toLowerCase()

                val filterResults = FilterResults()
                filterResults.values = if (queryString == null || queryString.isEmpty()) allLanguage
                else allLanguage.filter {
                    it.name.toLowerCase().contains(queryString)
                }
                return filterResults
            }
        }
    }
}