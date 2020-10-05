package com.zukron.mangara.repository.factory

import androidx.paging.DataSource
import com.zukron.mangara.model.PopularMangaResponse
import com.zukron.mangara.repository.AllContentRepository
import com.zukron.mangara.repository.datasource.PopularMangaDataSource
import io.reactivex.rxjava3.disposables.CompositeDisposable

/**
 * Project name is Manga Ra
 * Created by Zukron Alviandy R on 9/29/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
class PopularMangaDataFactory(
    private val allContentRepository: AllContentRepository,
    private val compositeDisposable: CompositeDisposable
) : DataSource.Factory<Int, PopularMangaResponse.Manga>() {
    override fun create(): DataSource<Int, PopularMangaResponse.Manga> {
        return PopularMangaDataSource(allContentRepository, compositeDisposable)
    }
}