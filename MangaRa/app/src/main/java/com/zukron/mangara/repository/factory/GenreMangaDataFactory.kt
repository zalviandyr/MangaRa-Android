package com.zukron.mangara.repository.factory

import androidx.paging.DataSource
import com.zukron.mangara.model.GenreMangaResponse
import com.zukron.mangara.repository.AllContentRepository
import com.zukron.mangara.repository.datasource.GenreMangaDataSource
import io.reactivex.rxjava3.disposables.CompositeDisposable

/**
 * Project name is Manga Ra
 * Created by Zukron Alviandy R on 9/29/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
class GenreMangaDataFactory(
    private val genreEndpoint: String,
    private val allContentRepository: AllContentRepository,
    private val compositeDisposable: CompositeDisposable
) : DataSource.Factory<Int, GenreMangaResponse.Manga>() {
    override fun create(): DataSource<Int, GenreMangaResponse.Manga> {
        return GenreMangaDataSource(genreEndpoint, allContentRepository, compositeDisposable)
    }
}