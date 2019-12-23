package com.epam.epmrduacmvan.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.epam.epmrduacmvan.R
import com.epam.epmrduacmvan.model.City

class CitySearchRecyclerViewAdapter: RecyclerView.Adapter<CitySearchRecyclerViewAdapter.CityViewHolder>() {
    private var cities: MutableList<City> = mutableListOf()
    private lateinit var mOnClickListener: View.OnClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.city_item, parent, false)
        view.setOnClickListener(mOnClickListener)
        return CityViewHolder(view)
    }

    fun setOnClickListener(listener: View.OnClickListener){
        mOnClickListener = listener
    }

    override fun getItemCount(): Int {
        return cities.size
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        holder.cityName.text = cities[position].name
        holder.countryName.text = cities[position].country?.name
    }

    fun setData(cities: List<City>) {
        this.cities.clear()
        this.cities.addAll(cities)
        notifyDataSetChanged()
    }

    inner class CityViewHolder(item: View): RecyclerView.ViewHolder(item) {
        val cityName: TextView = item.findViewById(R.id.city_name)
        val countryName: TextView = item.findViewById(R.id.country_name)
    }
}