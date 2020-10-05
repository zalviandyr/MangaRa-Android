package com.zukron.mangara.repository.datasource

import android.util.Log
import androidx.paging.PositionalDataSource
import com.zukron.mangara.model.SearchMangaResponse
import com.zukron.mangara.network.NetworkState
import com.zukron.mangara.repository.AllContentRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable

/**
 * Project name is Manga Ra
 * Created by Zukron Alviandy R on 9/29/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
class SearchMangaDataSource(
    private val keyword: String,
    private val allContentRepository: AllContentRepository,
    private val compositeDisposable: CompositeDisposable
) : PositionalDataSource<SearchMangaResponse.SearchMangaResponseItem>() {
    private val networkState = allContentRepository.networkState

    override fun loadInitial(
        params: LoadInitialParams,
        callback: LoadInitialCallback<SearchMangaResponse.SearchMangaResponseItem>
    ) {
        compositeDisposable.add(
            allContentRepository.getSearchManga(keyword)
                .subscribe({
                    Log.d("SearchMangaDataSource", "loadInitial: Masuk")
                    callback.onResult(it, 0)
                    networkState.postValue(NetworkState.LOADED)
                }, {
                    networkState.postValue(NetworkState.ERROR)
                })
        )
    }

    override fun loadRange(
        params: LoadRangeParams,
        callback: LoadRangeCallback<SearchMangaResponse.SearchMangaResponseItem>
    ) {
        // who care
    }
}