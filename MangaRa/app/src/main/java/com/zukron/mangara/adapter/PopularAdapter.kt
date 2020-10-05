package com.zukron.mangara.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.zukron.mangara.R
import com.zukron.mangara.adapter.listener.OnSelectedMangaListener
import com.zukron.mangara.model.PopularMangaResponse
import com.zukron.mangara.tools.Utilities
import kotlinx.android.synthetic.main.item_popular_manga.view.*

/**
 * Project name is Manga Ra
 * Created by Zukron Alviandy R on 9/28/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
class PopularAdapter :
    RecyclerView.Adapter<PopularAdapter.ViewHolder>() {

    private var onSelectedMangaListener: OnSelectedMangaListener? = null
    var popularMangaResponseList: List<PopularMangaResponse.Manga>? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val circularProgressDrawable =
            Utilities.circularProgressDrawable(itemView.context)

        fun bindTo(mangaResponse: PopularMangaResponse.Manga?) {
            mangaResponse?.let {
                itemView.simpleMangaItem_tvTitle.text = it.title
                itemView.simpleMangaItem_tvType.text = it.type

                // background type
                itemView.simpleMangaItem_tvType.background =
                    Utilities.backgroundType(it.type, itemView.context)

                Glide.with(itemView.context)
                    .load(it.thumb)
                    .placeholder(circularProgressDrawable)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(itemView.simpleMangaItem_imageView)

                itemView.setOnClickListener { _ ->
                    onSelectedMangaListener?.onSelectedManga(it.endpoint)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_popular_manga, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(popularMangaResponseList?.get(position))
    }

    override fun getItemCount(): Int {
        return popularMangaResponseList?.size ?: 0
    }

    fun setOnSelectedMangaListener(onSelectedMangaListener: OnSelectedMangaListener) {
        this.onSelectedMangaListener = onSelectedMangaListener
    }
}