package com.zukron.mangara.repository.datasource

import androidx.paging.PageKeyedDataSource
import com.zukron.mangara.model.GenreMangaResponse
import com.zukron.mangara.network.NetworkState
import com.zukron.mangara.repository.AllContentRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable

/**
 * Project name is Manga Ra
 * Created by Zukron Alviandy R on 9/29/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
class GenreMangaDataSource(
    private val genreEndpoint: String,
    private val allContentRepository: AllContentRepository,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Int, GenreMangaResponse.Manga>() {

    private var firstPage = 1
    private val networkState = allContentRepository.networkState

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, GenreMangaResponse.Manga>
    ) {
        compositeDisposable.add(
            allContentRepository.getAllGenreManga(genreEndpoint, firstPage)
                .subscribe({
                    callback.onResult(
                        it.data, null, firstPage + 1
                    )
                    networkState.postValue(NetworkState.LOADED)
                }, {
                    networkState.postValue(NetworkState.ERROR)
                })
        )
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, GenreMangaResponse.Manga>
    ) {
        // who care
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, GenreMangaResponse.Manga>
    ) {
        compositeDisposable.add(
            allContentRepository.getAllGenreManga(genreEndpoint, params.key)
                .subscribe({
                    callback.onResult(
                        it.data, params.key + 1
                    )
                    networkState.postValue(NetworkState.LOADED)
                }, {
                    networkState.postValue(NetworkState.ERROR)
                })
        )
    }
}