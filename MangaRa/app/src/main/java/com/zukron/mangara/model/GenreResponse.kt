package com.zukron.mangara.model

import com.google.gson.annotations.SerializedName

data class GenreResponse(
    @SerializedName("list_genre")
    val data: List<Genre>
) {
    data class Genre(
        @SerializedName("endpoint")
        val endpoint: String,
        @SerializedName("genre_name")
        val title: String
    )
}