package com.example.wikigamemvi.feature.game

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.wikigamemvi.R
import com.example.wikigamemvi.data.model.WikiUrl
import kotlinx.android.synthetic.main.wiki_navigation_item.view.*

class ArticleLinksAdapter(
    private val onItemClick: (WikiUrl) -> Unit
): ListAdapter<WikiUrl, ArticleLinksAdapter.ViewHolder>(WikiUrlDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.wiki_navigation_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun bind(wikiUrl: WikiUrl){
            itemView.nagivation_name.text = wikiUrl

            itemView.nagivation_container.setOnClickListener {
                onItemClick(wikiUrl)
            }
        }
    }
}

class WikiUrlDiffCallback: DiffUtil.ItemCallback<WikiUrl>(){
    override fun areItemsTheSame(oldItem: WikiUrl, newItem: WikiUrl): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: WikiUrl, newItem: WikiUrl): Boolean {
        return oldItem == newItem
    }
}