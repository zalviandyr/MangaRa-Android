package com.zukron.mangara.repository.factory

import androidx.paging.DataSource
import com.zukron.mangara.model.ChapterMangaResponse
import com.zukron.mangara.repository.AllContentRepository
import com.zukron.mangara.repository.datasource.ChapterImageDataSource
import io.reactivex.rxjava3.disposables.CompositeDisposable

/**
 * Project name is Manga Ra
 * Created by Zukron Alviandy R on 9/29/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
class ChapterImageDataFactory(
    private val endpoint: String,
    private val allContentRepository: AllContentRepository,
    private val compositeDisposable: CompositeDisposable
) : DataSource.Factory<Int, ChapterMangaResponse.ChapterImage>() {
    override fun create(): DataSource<Int, ChapterMangaResponse.ChapterImage> {
        return ChapterImageDataSource(endpoint, allContentRepository, compositeDisposable)
    }
}