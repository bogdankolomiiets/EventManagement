package com.epam.epmrduacmvan.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.LayoutRes
import com.epam.epmrduacmvan.R
import com.epam.epmrduacmvan.model.City

class CityAdapter(context: Context, @LayoutRes private val layoutResource: Int, private val allCity: List<City>) :
    ArrayAdapter<City>(context, layoutResource, allCity), Filterable {
    private var listCity: List<City> = allCity

    override fun getCount(): Int {
        return listCity.size
    }

    override fun getItem(position: Int): City? {
        return listCity[position]
    }

    override fun getItemId(position: Int): Long {
        return listCity[position].id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view =
            convertView as LinearLayout? ?: LayoutInflater.from(context).inflate(layoutResource,
                parent, false) as LinearLayout
        view.findViewById<TextView>(R.id.text_line_city).text = listCity[position].name
        view.findViewById<TextView>(R.id.text_line_country).text =
            listCity[position].country?.name?.toUpperCase()
        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
                listCity = filterResults.values as List<City>
                notifyDataSetChanged()
            }

            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val queryString = charSequence?.toString()?.toLowerCase()

                val filterResults = FilterResults()
                filterResults.values = if (queryString == null || queryString.isEmpty()) allCity
                else allCity.filter {
                    it.name.toLowerCase().contains(queryString)
                }
                return filterResults
            }
        }
    }
}