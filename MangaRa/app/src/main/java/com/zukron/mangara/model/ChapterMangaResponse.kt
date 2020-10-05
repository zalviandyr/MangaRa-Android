package com.zukron.mangara.model

import com.google.gson.annotations.SerializedName

data class ChapterMangaResponse(
    @SerializedName("chapter_endpoint")
    val chapterEndpoint: String,
    @SerializedName("chapter_image")
    val chapterImage: List<ChapterImage>,
    @SerializedName("title")
    val title: String
) {
    data class ChapterImage(
        @SerializedName("chapter_image_link")
        val chapterImageLink: String,
        @SerializedName("image_number")
        val imageNumber: Int
    )
}