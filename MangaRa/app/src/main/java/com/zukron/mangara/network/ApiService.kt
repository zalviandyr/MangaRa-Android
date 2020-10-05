package com.zukron.mangara.network

import com.zukron.mangara.model.*
import io.reactivex.rxjava3.core.Flowable
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Project name is Manga Ra
 * Created by Zukron Alviandy R on 9/28/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
interface ApiService {
    @GET("manga/popular/{page}")
    fun getPopularManga(
        @Path("page") page: Int
    ): Flowable<PopularMangaResponse>

    @GET("genres")
    fun getGenre(): Flowable<GenreResponse>

    @GET("manga/page/{page}")
    fun getManga(
        @Path("page") page: Int
    ): Flowable<MangaResponse>

    @GET("manhua/{page}")
    fun getManhua(
        @Path("page") page: Int
    ): Flowable<MangaResponse>

    @GET("manhwa/{page}")
    fun getManhwa(
        @Path("page") page: Int
    ): Flowable<MangaResponse>

    @GET("genres/{endpoint}/{page}")
    fun getGenreManga(
        @Path("endpoint") endpoint: String,
        @Path("page") page: Int
    ): Flowable<GenreMangaResponse>

    @GET("cari/{keyword}")
    fun getSearchManga(
        @Path("keyword") keyword: String
    ): Flowable<SearchMangaResponse>

    @GET("manga/detail/{endpoint}")
    fun getDetailManga(
        @Path("endpoint") endpoint: String
    ): Flowable<DetailMangaResponse>

    @GET("chapter/{endpoint}")
    fun getChapterImage(
        @Path("endpoint") endpoint: String
    ): Flowable<ChapterMangaResponse>
}