package com.zukron.mangara.repository.datasource

import androidx.paging.PageKeyedDataSource
import com.zukron.mangara.model.MangaResponse
import com.zukron.mangara.model.helper.MangaHelper
import com.zukron.mangara.network.NetworkState
import com.zukron.mangara.repository.AllContentRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable

/**
 * Project name is Manga Ra
 * Created by Zukron Alviandy R on 9/29/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
class MangaDataSource(
    private val type: String,
    private val allContentRepository: AllContentRepository,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Int, MangaResponse.Manga>() {

    private var firstPage = 1
    private val networkState = allContentRepository.networkState

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, MangaResponse.Manga>
    ) {
        when (type) {
            MangaHelper.Type.manga -> {
                compositeDisposable.add(
                    allContentRepository.getAllManga(firstPage)
                        .map {
                            it.data.filter {
                                it.type == MangaHelper.Type.manga
                            }
                        }
                        .subscribe({
                            callback.onResult(
                                it, null, firstPage + 1
                            )
                            networkState.postValue(NetworkState.LOADED)
                        }, {
                            networkState.postValue(NetworkState.ERROR)
                        })
                )
            }
            MangaHelper.Type.manhua -> {
                compositeDisposable.add(
                    allContentRepository.getAllManhua(firstPage)
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
            MangaHelper.Type.manhwa -> {
                compositeDisposable.add(
                    allContentRepository.getAllManhwa(firstPage)
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
        }
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, MangaResponse.Manga>
    ) {
        // who care
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, MangaResponse.Manga>
    ) {
        when (type) {
            MangaHelper.Type.manga -> {
                compositeDisposable.add(
                    allContentRepository.getAllManga(params.key)
                        .map {
                            it.data.filter {
                                it.type == MangaHelper.Type.manga
                            }
                        }
                        .subscribe({
                            callback.onResult(
                                it, params.key + 1
                            )
                            networkState.postValue(NetworkState.LOADED)
                        }, {
                            networkState.postValue(NetworkState.ERROR)
                        })
                )
            }
            MangaHelper.Type.manhua -> {
                compositeDisposable.add(
                    allContentRepository.getAllManhua(params.key)
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
            MangaHelper.Type.manhwa -> {
                compositeDisposable.add(
                    allContentRepository.getAllManhwa(params.key)
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
    }
}