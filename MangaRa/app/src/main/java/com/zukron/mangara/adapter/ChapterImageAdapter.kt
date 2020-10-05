package com.zukron.mangara.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.Target
import com.zukron.mangara.R
import com.zukron.mangara.model.ChapterMangaResponse
import com.zukron.mangara.tools.Utilities
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import kotlinx.android.synthetic.main.item_chapter_manga.view.*

/**
 * Project name is Manga Ra
 * Created by Zukron Alviandy R on 10/1/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
class ChapterImageAdapter
    : PagedListAdapter<ChapterMangaResponse.ChapterImage, ChapterImageAdapter.ViewHolder>(ItemDiffCallback) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val circularProgressDrawable =
            Utilities.circularProgressDrawable(itemView.context)

        fun bindTo(chapterImage: ChapterMangaResponse.ChapterImage?) {
            chapterImage?.let {
                Glide.with(itemView.context)
                    .load(it.chapterImageLink)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(circularProgressDrawable)
                    .error(R.drawable.failed_load_image)
                    .override(Target.SIZE_ORIGINAL)
                    .into(itemView.chapterMangaItem_imageView)
            }
        }
    }

    private object ItemDiffCallback : DiffUtil.ItemCallback<ChapterMangaResponse.ChapterImage>() {
        override fun areItemsTheSame(
            oldItem: ChapterMangaResponse.ChapterImage,
            newItem: ChapterMangaResponse.ChapterImage
        ): Boolean {
            return oldItem.imageNumber == newItem.imageNumber
        }

        override fun areContentsTheSame(
            oldItem: ChapterMangaResponse.ChapterImage,
            newItem: ChapterMangaResponse.ChapterImage
        ): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_chapter_manga, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }
}