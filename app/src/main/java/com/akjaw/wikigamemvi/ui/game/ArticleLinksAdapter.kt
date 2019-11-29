package com.akjaw.wikigamemvi.ui.game

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.akjaw.wikigamemvi.R
import kotlinx.android.synthetic.main.wiki_navigation_item.view.*

class ArticleLinksAdapter(
    private val onItemClick: (com.akjaw.domain.model.WikiTitle) -> Unit
): ListAdapter<com.akjaw.domain.model.WikiTitle, ArticleLinksAdapter.ViewHolder>(WikiTitleDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.wiki_navigation_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun bind(wikiTitle: com.akjaw.domain.model.WikiTitle){
            itemView.nagivation_name.text = wikiTitle

            itemView.nagivation_container.setOnClickListener {
                onItemClick(wikiTitle)
            }
        }
    }
}

class WikiTitleDiffCallback: DiffUtil.ItemCallback<com.akjaw.domain.model.WikiTitle>(){
    override fun areItemsTheSame(oldItem: com.akjaw.domain.model.WikiTitle, newItem: com.akjaw.domain.model.WikiTitle): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: com.akjaw.domain.model.WikiTitle, newItem: com.akjaw.domain.model.WikiTitle): Boolean {
        return oldItem == newItem
    }
}