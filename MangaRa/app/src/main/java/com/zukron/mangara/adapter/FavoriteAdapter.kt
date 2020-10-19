package com.zukron.mangara.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.zukron.mangara.R
import com.zukron.mangara.adapter.listener.OnSelectedMangaListener
import com.zukron.mangara.model.helper.FavoriteMangaHelper
import com.zukron.mangara.tools.Utilities
import kotlinx.android.synthetic.main.item_manga.view.*

/**
 * Project name is Manga Ra
 * Created by Zukron Alviandy R on 10/4/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
class FavoriteAdapter(
    private val favoriteMangaList: List<FavoriteMangaHelper>,
    private var onSelectedMangaListener: OnSelectedMangaListener
) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val circularProgressDrawable =
            Utilities.circularProgressDrawable(itemView.context)

        fun bindTo(favoriteManga: FavoriteMangaHelper) {
            itemView.mangaItem_tvTitle.text = favoriteManga.title
            itemView.mangaItem_tvType.text = favoriteManga.type

            // background type
            itemView.mangaItem_tvType.background =
                Utilities.backgroundType(favoriteManga.type, itemView.context)

            // set image view height
            val layoutParams = itemView.mangaItem_imageView.layoutParams
            layoutParams.width = itemView.context.resources.getDimension(R.dimen.thumbnail_fav_history_width).toInt()

            Glide.with(itemView.context)
                .load(favoriteManga.thumb)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(circularProgressDrawable)
                .into(itemView.mangaItem_imageView)

            itemView.setOnClickListener {
                onSelectedMangaListener.onSelectedManga(favoriteManga.endpoint)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_manga, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(favoriteMangaList.get(position))
    }

    override fun getItemCount(): Int {
        return favoriteMangaList.size
    }
}