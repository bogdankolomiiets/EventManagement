package com.epam.epmrduacmvan.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.LayoutRes
import com.epam.epmrduacmvan.R
import com.epam.epmrduacmvan.model.Country

class CountryAdapter(context: Context, @LayoutRes private val layoutResource: Int, private val allCountry: List<Country>) :
    ArrayAdapter<Country>(context, layoutResource, allCountry), Filterable {
    private var listCountry: List<Country> = allCountry

    override fun getCount(): Int {
        return listCountry.size
    }

    override fun getItem(position: Int): Country? {
        return listCountry[position]
    }

    override fun getItemId(position: Int): Long {
        return listCountry[position].id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView as LinearLayout? ?: LayoutInflater.from(context).inflate(layoutResource, parent,false) as LinearLayout
        view.findViewById<TextView>(R.id.text_line_search).text = listCountry[position].name
        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
                listCountry = filterResults.values as List<Country>
                notifyDataSetChanged()
            }

            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val queryString = charSequence?.toString()?.toLowerCase()

                val filterResults = FilterResults()
                filterResults.values = if (queryString == null || queryString.isEmpty()) allCountry
                else allCountry.filter {
                    it.name.toLowerCase().contains(queryString)
                }
                return filterResults
            }
        }
    }
}