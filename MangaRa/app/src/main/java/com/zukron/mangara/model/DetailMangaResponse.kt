package com.zukron.mangara.model

import com.google.gson.annotations.SerializedName

data class DetailMangaResponse(
    @SerializedName("author")
    val author: String,
    @SerializedName("chapter")
    val chapter: List<Chapter>,
    @SerializedName("genre_list")
    val genreList: List<Genre>,
    @SerializedName("manga_endpoint")
    val mangaEndpoint: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("synopsis")
    val synopsis: String,
    @SerializedName("thumb")
    val thumb: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("type")
    val type: String
) {
    data class Chapter(
        @SerializedName("chapter_endpoint")
        val chapterEndpoint: String,
        @SerializedName("chapter_title")
        val chapterTitle: String
    )

    data class Genre(
        @SerializedName("genre_name")
        val genreName: String
    )
}