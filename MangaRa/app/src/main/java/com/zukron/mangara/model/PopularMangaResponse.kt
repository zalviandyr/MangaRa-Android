package com.zukron.mangara.model

import com.google.gson.annotations.SerializedName

/**
 * Project name is Manga Ra
 * Created by Zukron Alviandy R on 9/28/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
data class PopularMangaResponse(
    @SerializedName("manga_list")
    val data: List<Manga>
) {
    data class Manga(
        @SerializedName("title")
        val title: String,
        @SerializedName("type")
        val type: String,
        @SerializedName("thumb")
        val thumb: String,
        @SerializedName("endpoint")
        val endpoint: String,
        @SerializedName("upload_on")
        val uploadOn: String
    )
}