package com.zukron.mangara.repository.factory

import androidx.paging.DataSource
import com.zukron.mangara.model.MangaResponse
import com.zukron.mangara.repository.AllContentRepository
import com.zukron.mangara.repository.datasource.MangaDataSource
import io.reactivex.rxjava3.disposables.CompositeDisposable

/**
 * Project name is Manga Ra
 * Created by Zukron Alviandy R on 9/29/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
class MangaDataFactory(
    private val type: String,
    private val allContentRepository: AllContentRepository,
    private val compositeDisposable: CompositeDisposable
) : DataSource.Factory<Int, MangaResponse.Manga>() {
    override fun create(): DataSource<Int, MangaResponse.Manga> {
        return MangaDataSource(type, allContentRepository, compositeDisposable)
    }
}