package com.epam.epmrduacmvan.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.LayoutRes
import com.epam.epmrduacmvan.AppApplication
import com.epam.epmrduacmvan.R
import com.epam.epmrduacmvan.model.Speakers

class SpeakersAdapter(context: Context, @LayoutRes private val layoutResource: Int, private val allSpeakers: List<Speakers>) :
    ArrayAdapter<Speakers>(context, layoutResource, allSpeakers), Filterable {
    private var listSpeakers: List<Speakers> = allSpeakers

    override fun getCount(): Int {
        return listSpeakers.size
    }

    override fun getItem(position: Int): Speakers? {
        return listSpeakers[position]
    }

    override fun getItemId(position: Int): Long {
        return listSpeakers[position].id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView as LinearLayout? ?: LayoutInflater.from(context).inflate(layoutResource, parent,false) as LinearLayout
        view.findViewById<TextView>(R.id.text_line_search).text = AppApplication.appContext.getString(R.string.speakers_name_and_surname, listSpeakers[position].lastName, listSpeakers[position].firstName)
        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
                listSpeakers = filterResults.values as List<Speakers>
                notifyDataSetChanged()
            }

            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val queryString = charSequence?.toString()?.toLowerCase()

                val filterResults = FilterResults()
                filterResults.values = if (queryString == null || queryString.isEmpty()) allSpeakers
                else allSpeakers.filter {
                    it.lastName.toLowerCase().contains(queryString) || it.firstName.toLowerCase().contains(queryString)
                }
                return filterResults
            }
        }
    }
}