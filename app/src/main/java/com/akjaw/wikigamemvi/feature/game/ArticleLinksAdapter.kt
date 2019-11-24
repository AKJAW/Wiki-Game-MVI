package com.akjaw.wikigamemvi.feature.game

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.akjaw.wikigamemvi.R
import com.akjaw.wikigamemvi.data.model.WikiTitle
import kotlinx.android.synthetic.main.wiki_navigation_item.view.*

class ArticleLinksAdapter(
    private val onItemClick: (WikiTitle) -> Unit
): ListAdapter<WikiTitle, ArticleLinksAdapter.ViewHolder>(WikiTitleDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.wiki_navigation_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun bind(wikiTitle: WikiTitle){
            itemView.nagivation_name.text = wikiTitle

            itemView.nagivation_container.setOnClickListener {
                onItemClick(wikiTitle)
            }
        }
    }
}

class WikiTitleDiffCallback: DiffUtil.ItemCallback<WikiTitle>(){
    override fun areItemsTheSame(oldItem: WikiTitle, newItem: WikiTitle): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: WikiTitle, newItem: WikiTitle): Boolean {
        return oldItem == newItem
    }
}