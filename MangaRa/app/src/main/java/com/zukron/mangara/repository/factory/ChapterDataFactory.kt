package com.zukron.mangara.repository.factory

import androidx.paging.DataSource
import com.zukron.mangara.model.DetailMangaResponse
import com.zukron.mangara.repository.AllContentRepository
import com.zukron.mangara.repository.datasource.ChapterDataSource
import io.reactivex.rxjava3.disposables.CompositeDisposable

/**
 * Project name is Manga Ra
 * Created by Zukron Alviandy R on 9/29/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
class ChapterDataFactory(
    private val endpoint: String,
    private val allContentRepository: AllContentRepository,
    private val compositeDisposable: CompositeDisposable
) : DataSource.Factory<Int, DetailMangaResponse.Chapter>() {
    override fun create(): DataSource<Int, DetailMangaResponse.Chapter> {
        return ChapterDataSource(endpoint, allContentRepository, compositeDisposable)
    }
}