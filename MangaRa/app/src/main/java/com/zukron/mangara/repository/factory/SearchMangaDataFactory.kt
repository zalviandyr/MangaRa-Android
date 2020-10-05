package com.zukron.mangara.repository.factory

import androidx.paging.DataSource
import com.zukron.mangara.model.MangaResponse
import com.zukron.mangara.model.SearchMangaResponse
import com.zukron.mangara.repository.AllContentRepository
import com.zukron.mangara.repository.datasource.SearchMangaDataSource
import io.reactivex.rxjava3.disposables.CompositeDisposable

/**
 * Project name is Manga Ra
 * Created by Zukron Alviandy R on 9/29/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
class SearchMangaDataFactory(
    private val keyword: String,
    private val allContentRepository: AllContentRepository,
    private val compositeDisposable: CompositeDisposable
) : DataSource.Factory<Int, SearchMangaResponse.SearchMangaResponseItem>() {
    override fun create(): DataSource<Int, SearchMangaResponse.SearchMangaResponseItem> {
        return SearchMangaDataSource(keyword, allContentRepository, compositeDisposable)
    }
}