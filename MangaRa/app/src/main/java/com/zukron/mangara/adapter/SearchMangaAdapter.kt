package com.zukron.mangara.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zukron.mangara.R
import com.zukron.mangara.adapter.listener.OnSelectedMangaListener
import com.zukron.mangara.model.SearchMangaResponse
import com.zukron.mangara.tools.Utilities
import kotlinx.android.synthetic.main.item_manga.view.*

/**
 * Project name is Manga Ra
 * Created by Zukron Alviandy R on 10/18/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
class SearchMangaAdapter(
    private val mangaList: List<SearchMangaResponse.SearchMangaResponseItem>,
    private val onSelectedMangaListener: OnSelectedMangaListener
): RecyclerView.Adapter<SearchMangaAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val circularProgressDrawable =
            Utilities.circularProgressDrawable(itemView.context)

        fun bindTo(searchMangaResponseItem: SearchMangaResponse.SearchMangaResponseItem) {
            itemView.mangaItem_tvTitle.text = searchMangaResponseItem.title
            itemView.mangaItem_tvType.text = searchMangaResponseItem.type
            itemView.mangaItem_tvUpdateOn.text = searchMangaResponseItem.updatedOn

            // background type
            itemView.mangaItem_tvType.background =
                Utilities.backgroundType(searchMangaResponseItem.type, itemView.context)

            Glide.with(itemView.context)
                .load(searchMangaResponseItem.thumb)
                .placeholder(circularProgressDrawable)
                .into(itemView.mangaItem_imageView)

            // listener
            itemView.setOnClickListener {
                onSelectedMangaListener.onSelectedManga(searchMangaResponseItem.endpoint)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_manga, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(mangaList[position])
    }

    override fun getItemCount(): Int {
        return mangaList.size
    }
}