package com.epam.epmrduacmvan.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.epam.epmrduacmvan.R
import com.epam.epmrduacmvan.model.Language
import com.epam.epmrduacmvan.utils.LanguageItemClickListener
import java.util.*

class LanguageRecycleViewAdapter(private val languageItemClickListener: LanguageItemClickListener): RecyclerView.Adapter<LanguageRecycleViewAdapter.LanguageViewHolder>() {
    private var previousHolder: LanguageViewHolder? = null
    private var languages: MutableList<Language> = mutableListOf()
    private var systemLanguage = Locale.getDefault().isO3Language.toUpperCase(Locale.getDefault())
    private var selectedLanguage = systemLanguage

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.language_item, parent, false)
        return LanguageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return languages.size
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        val language = languages[position]
        holder.languageName.text = language.name
        if (language.name == systemLanguage && previousHolder == null) {
            previousHolder = holder
            holder.itemView.isActivated = true
            languageItemClickListener.onItemClick(language)
        }

        holder.itemView.setOnClickListener{
            setNormalBackground()
            previousHolder = holder
            holder.itemView.isActivated = true
            selectedLanguage = language.name
            languageItemClickListener.onItemClick(language)
        }
    }

    fun setData(languages: List<Language>) {
        this.languages.clear()
        this.languages.addAll(languages)
        notifyDataSetChanged()
    }

    override fun onViewRecycled(holder: LanguageViewHolder) {
        super.onViewRecycled(holder)
        if (holder == previousHolder) {
            setNormalBackground()
        }
    }

    private fun setNormalBackground() {
        previousHolder?.itemView?.isActivated = false
    }

    inner class LanguageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val languageName: TextView = itemView.findViewById(R.id.text_language_name)
    }
}