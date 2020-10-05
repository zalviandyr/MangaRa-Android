package com.zukron.mangara.model


import com.google.gson.annotations.SerializedName

class SearchMangaResponse : ArrayList<SearchMangaResponse.SearchMangaResponseItem>() {
    data class SearchMangaResponseItem(
        @SerializedName("endpoint")
        val endpoint: String,
        @SerializedName("thumb")
        val thumb: String,
        @SerializedName("title")
        val title: String,
        @SerializedName("type")
        val type: String,
        @SerializedName("updated_on")
        val updatedOn: String
    )
}