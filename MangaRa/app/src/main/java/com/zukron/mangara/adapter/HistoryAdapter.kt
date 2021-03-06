package com.zukron.mangara.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.zukron.mangara.R
import com.zukron.mangara.adapter.listener.OnSelectedHistoryListener
import com.zukron.mangara.model.helper.HistoryMangaHelper
import com.zukron.mangara.tools.Utilities
import kotlinx.android.synthetic.main.item_manga.view.*

/**
 * Project name is Manga Ra
 * Created by Zukron Alviandy R on 10/5/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
class HistoryAdapter(
    private val historyList: List<HistoryMangaHelper>,
    private var onSelectedHistoryListener: OnSelectedHistoryListener
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val circularProgressDrawable = Utilities.circularProgressDrawable(itemView.context)

        fun bindTo(historyMangaHelper: HistoryMangaHelper) {
            itemView.mangaItem_tvTitle.text = historyMangaHelper.title
            itemView.mangaItem_tvType.text = historyMangaHelper.type
            itemView.mangaItem_tvChapter.text = itemView.context.getString(R.string.last_chapter)
            itemView.mangaItem_tvUpdateOn.text = historyMangaHelper.chapter

            // background type
            itemView.mangaItem_tvType.background =
                Utilities.backgroundType(historyMangaHelper.type, itemView.context)

            // set image view height
            val layoutParams = itemView.mangaItem_imageView.layoutParams
            layoutParams.width = itemView.context.resources.getDimension(R.dimen.thumbnail_fav_history_width).toInt()

            Glide.with(itemView.context)
                .load(historyMangaHelper.thumb)
                .placeholder(circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(itemView.mangaItem_imageView)

            itemView.setOnClickListener {
                onSelectedHistoryListener.onSelectedHistory(historyMangaHelper)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_manga, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(historyList[position])
    }

    override fun getItemCount(): Int {
        return historyList.size
    }
}