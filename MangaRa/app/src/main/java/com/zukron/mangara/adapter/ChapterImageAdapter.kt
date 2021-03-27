package com.zukron.mangara.adapter

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.zukron.mangara.R
import com.zukron.mangara.model.ChapterMangaResponse
import com.zukron.mangara.tools.Utilities
import kotlinx.android.synthetic.main.item_chapter_manga.view.*

/**
 * Project name is Manga Ra
 * Created by Zukron Alviandy R on 10/18/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
class ChapterImageAdapter(private val chapterImageList: List<ChapterMangaResponse.ChapterImage>) :
    RecyclerView.Adapter<ChapterImageAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val circularProgressDrawable =
            Utilities.circularProgressDrawable(itemView.context)

        fun bindTo(chapterImage: ChapterMangaResponse.ChapterImage) {
//            itemView.chapterMangaItem_imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)

            Glide.with(itemView.context)
                .asBitmap()
                .load(chapterImage.chapterImageLink)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(circularProgressDrawable)
                .error(R.drawable.failed_load_image)
                .override(Target.SIZE_ORIGINAL)
                .into(itemView.chapterMangaItem_imageView)
//                .into(object : CustomTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
//                    override fun onResourceReady(
//                        resource: Bitmap,
//                        transition: Transition<in Bitmap>?
//                    ) {
////                        itemView.chapterMangaItem_imageView.post {
////                            itemView.chapterMangaItem_imageView.setImageBitmap(
////                                resource
////                            )
////                        }
//                        itemView.chapterMangaItem_imageView.setImageBitmap(resource)
//                    }
//
//                    override fun onLoadCleared(placeholder: Drawable?) {
//                        // who care
//                    }
//                })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_chapter_manga, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(chapterImageList[position])
    }

    override fun getItemCount(): Int {
        return chapterImageList.size
    }
}