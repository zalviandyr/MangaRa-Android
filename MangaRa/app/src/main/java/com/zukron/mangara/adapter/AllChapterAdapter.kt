package com.zukron.mangara.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zukron.mangara.R
import com.zukron.mangara.adapter.listener.OnSelectedChapterListener
import com.zukron.mangara.model.DetailMangaResponse
import kotlinx.android.synthetic.main.item_chapter.view.*

/**
 * Project name is Manga Ra
 * Created by Zukron Alviandy R on 10/18/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
class AllChapterAdapter(
    private val chapterList: List<DetailMangaResponse.Chapter>,
    private val onSelectedChapterListener: OnSelectedChapterListener
) : RecyclerView.Adapter<AllChapterAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindTo(chapter: DetailMangaResponse.Chapter) {
            itemView.chapterItem_tv.text = chapter.chapterTitle

            itemView.setOnClickListener {
                onSelectedChapterListener.onSelectedChapter(
                    chapter.chapterTitle,
                    chapter.chapterEndpoint
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_chapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(chapterList[position])
    }

    override fun getItemCount(): Int {
        return chapterList.size
    }
}