package com.zukron.mangara.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zukron.mangara.R
import com.zukron.mangara.adapter.listener.OnSelectedChapterListener
import com.zukron.mangara.model.DetailMangaResponse
import kotlinx.android.synthetic.main.item_chapter.view.*

/**
 * Project name is Manga Ra
 * Created by Zukron Alviandy R on 9/28/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
class AllChapterPagedListAdapter :
    PagedListAdapter<DetailMangaResponse.Chapter, AllChapterPagedListAdapter.ViewHolder>(
        ItemDiffUtil
    ) {

    private var onSelectedChapterListener: OnSelectedChapterListener? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindTo(chapter: DetailMangaResponse.Chapter?) {
            chapter?.let {
                itemView.chapterItem_tv.text = it.chapterTitle

                itemView.setOnClickListener { _ ->
                    onSelectedChapterListener?.onSelectedChapter(
                        it.chapterTitle,
                        it.chapterEndpoint
                    )
                }
            }
        }
    }

    private object ItemDiffUtil : DiffUtil.ItemCallback<DetailMangaResponse.Chapter>() {
        override fun areItemsTheSame(
            oldItem: DetailMangaResponse.Chapter,
            newItem: DetailMangaResponse.Chapter
        ): Boolean {
            return oldItem.chapterTitle == newItem.chapterTitle
        }

        override fun areContentsTheSame(
            oldItem: DetailMangaResponse.Chapter,
            newItem: DetailMangaResponse.Chapter
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_chapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    fun setOnSelectedChapterListener(onSelectedChapterListener: OnSelectedChapterListener) {
        this.onSelectedChapterListener = onSelectedChapterListener
    }
}