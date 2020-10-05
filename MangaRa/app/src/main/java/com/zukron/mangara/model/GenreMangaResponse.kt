package com.zukron.mangara.model

import com.google.gson.annotations.SerializedName

data class GenreMangaResponse(
    @SerializedName("manga_list")
    val data: List<Manga>
) {
    data class Manga(
        @SerializedName("endpoint")
        val endpoint: String,
        @SerializedName("thumb")
        val thumb: String,
        @SerializedName("title")
        val title: String,
        @SerializedName("type")
        val type: String
    )
}