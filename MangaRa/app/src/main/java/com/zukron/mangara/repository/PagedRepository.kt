package com.zukron.mangara.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.zukron.mangara.model.*
import com.zukron.mangara.model.helper.MangaHelper
import com.zukron.mangara.network.NetworkState
import com.zukron.mangara.repository.factory.GenreMangaDataFactory
import com.zukron.mangara.repository.factory.MangaDataFactory
import com.zukron.mangara.repository.factory.PopularMangaDataFactory
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.concurrent.Executors

/**
 * Project name is Manga Ra
 * Created by Zukron Alviandy R on 9/29/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
class PagedRepository(context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: PagedRepository? = null

        fun getInstance(context: Context): PagedRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: PagedRepository(context).also { INSTANCE = it }
            }
    }

    private val allContentRepository = AllContentRepository.getInstance(context)
    private val postPerPage = 10
    private val executor = Executors.newFixedThreadPool(5)
    private val pageListConfig = PagedList.Config.Builder()
        .setPageSize(postPerPage)
        .setEnablePlaceholders(false)
        .build()
    private val _networkState = allContentRepository.networkState

    val networkState: LiveData<NetworkState>
        get() {
            // tidak tau kenapa yang kepanggil adalah loaded duluan,
            // maka ny harus di ubah ke loading
            _networkState.value = NetworkState.LOADING
            return _networkState
        }

    fun allPopularManga(
        compositeDisposable: CompositeDisposable
    ): LiveData<PagedList<PopularMangaResponse.Manga>> {
        val popularMangaDataFactory =
            PopularMangaDataFactory(allContentRepository, compositeDisposable)

        return LivePagedListBuilder(popularMangaDataFactory, pageListConfig)
            .setFetchExecutor(executor)
            .build()
    }

    fun allManga(
        compositeDisposable: CompositeDisposable
    ): LiveData<PagedList<MangaResponse.Manga>> {
        val mangaDataFactory =
            MangaDataFactory(MangaHelper.Type.manga, allContentRepository, compositeDisposable)

        return LivePagedListBuilder(mangaDataFactory, pageListConfig)
            .setFetchExecutor(executor)
            .build()
    }

    fun allManhua(
        compositeDisposable: CompositeDisposable
    ): LiveData<PagedList<MangaResponse.Manga>> {
        val mangaDataFactory =
            MangaDataFactory(MangaHelper.Type.manhua, allContentRepository, compositeDisposable)

        return LivePagedListBuilder(mangaDataFactory, pageListConfig)
            .setFetchExecutor(executor)
            .build()
    }

    fun allManhwa(
        compositeDisposable: CompositeDisposable
    ): LiveData<PagedList<MangaResponse.Manga>> {
        val mangaDataFactory =
            MangaDataFactory(MangaHelper.Type.manhwa, allContentRepository, compositeDisposable)

        return LivePagedListBuilder(mangaDataFactory, pageListConfig)
            .setFetchExecutor(executor)
            .build()
    }

    fun allGenreManga(
        genreEndpoint: String,
        compositeDisposable: CompositeDisposable
    ): LiveData<PagedList<GenreMangaResponse.Manga>> {
        val genreMangaDataFactory =
            GenreMangaDataFactory(genreEndpoint, allContentRepository, compositeDisposable)

        return LivePagedListBuilder(genreMangaDataFactory, pageListConfig)
            .setFetchExecutor(executor)
            .build()
    }
}