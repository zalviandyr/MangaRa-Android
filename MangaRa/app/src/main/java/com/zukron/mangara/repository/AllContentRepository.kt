package com.zukron.mangara.repository

import android.content.Context
import com.zukron.mangara.model.*
import com.zukron.mangara.network.NetworkState
import com.zukron.mangara.network.RestApi
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * Project name is Manga Ra
 * Created by Zukron Alviandy R on 9/29/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
class AllContentRepository(context: Context) {
    private var apiService = RestApi.getApiService(context)
    val networkState = RestApi.networkState

    companion object {
        @Volatile
        private var INSTANCE: AllContentRepository? = null

        fun getInstance(context: Context): AllContentRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: AllContentRepository(context).also { INSTANCE = it }
            }
    }

    fun getAllPopularManga(page: Int): Flowable<PopularMangaResponse> {
        networkState.postValue(NetworkState.LOADING)

        var i = 0
        return apiService.getPopularManga(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .timeout(5, TimeUnit.SECONDS)
            .retryWhen {
                it.takeWhile { v ->
                    // ada bug pada server mangamint, ketika mengambil data di page yang tidak ada
                    // maka server akan terus loading, hal itu menyebabkan app akan terus meminta data
                    // sampai data yang tidak pernah ada itu terambil
                    // maka dari itu kita harus membuat count jika sudah 3 kali time out
                    // maka sudahi request ke server
                    return@takeWhile if (v is TimeoutException || v is SocketTimeoutException) {
                        if (i == 3) {
                            networkState.postValue(NetworkState.END_OF_PAGE)
                            false
                        } else {
                            i++
                            networkState.postValue(NetworkState.TIMEOUT)
                            true
                        }
                    } else {
                        networkState.postValue(NetworkState.ERROR)
                        false
                    }
                }
            }
    }

    fun getAllManga(page: Int): Flowable<MangaResponse> {
        networkState.postValue(NetworkState.LOADING)

        var i = 0
        return apiService.getManga(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .timeout(5, TimeUnit.SECONDS)
            .retryWhen {
                it.takeWhile { v ->
                    // ada bug pada server mangamint, ketika mengambil data di page yang tidak ada
                    // maka server akan terus loading, hal itu menyebabkan app akan terus meminta data
                    // sampai data yang tidak pernah ada itu terambil
                    // maka dari itu kita harus membuat count jika sudah 3 kali time out
                    // maka sudahi request ke server
                    return@takeWhile if (v is TimeoutException || v is SocketTimeoutException) {
                        if (i == 3) {
                            networkState.postValue(NetworkState.END_OF_PAGE)
                            false
                        } else {
                            i++
                            networkState.postValue(NetworkState.TIMEOUT)
                            true
                        }
                    } else {
                        networkState.postValue(NetworkState.ERROR)
                        false
                    }
                }
            }
    }

    fun getAllManhua(page: Int): Flowable<MangaResponse> {
        networkState.postValue(NetworkState.LOADING)

        var i = 0
        return apiService.getManhua(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .timeout(5, TimeUnit.SECONDS)
            .retryWhen {
                it.takeWhile { v ->
                    // ada bug pada server mangamint, ketika mengambil data di page yang tidak ada
                    // maka server akan terus loading, hal itu menyebabkan app akan terus meminta data
                    // sampai data yang tidak pernah ada itu terambil
                    // maka dari itu kita harus membuat count jika sudah 3 kali time out
                    // maka sudahi request ke server
                    return@takeWhile if (v is TimeoutException || v is SocketTimeoutException) {
                        if (i == 3) {
                            networkState.postValue(NetworkState.END_OF_PAGE)
                            false
                        } else {
                            i++
                            networkState.postValue(NetworkState.TIMEOUT)
                            true
                        }
                    } else {
                        networkState.postValue(NetworkState.ERROR)
                        false
                    }
                }
            }
    }

    fun getAllManhwa(page: Int): Flowable<MangaResponse> {
        networkState.postValue(NetworkState.LOADING)

        var i = 0
        return apiService.getManhwa(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .timeout(5, TimeUnit.SECONDS)
            .retryWhen {
                it.takeWhile { v ->
                    // ada bug pada server mangamint, ketika mengambil data di page yang tidak ada
                    // maka server akan terus loading, hal itu menyebabkan app akan terus meminta data
                    // sampai data yang tidak pernah ada itu terambil
                    // maka dari itu kita harus membuat count jika sudah 3 kali time out
                    // maka sudahi request ke server
                    return@takeWhile if (v is TimeoutException || v is SocketTimeoutException) {
                        if (i == 3) {
                            networkState.postValue(NetworkState.END_OF_PAGE)
                            false
                        } else {
                            i++
                            networkState.postValue(NetworkState.TIMEOUT)
                            true
                        }
                    } else {
                        networkState.postValue(NetworkState.ERROR)
                        false
                    }
                }
            }
    }

    fun getAllGenreManga(genreEndpoint: String, page: Int): Flowable<GenreMangaResponse> {
        networkState.postValue(NetworkState.LOADING)

        var i = 0
        return apiService.getGenreManga(genreEndpoint, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .timeout(5, TimeUnit.SECONDS)
            .retryWhen {
                it.takeWhile { v ->
                    // ada bug pada server mangamint, ketika mengambil data di page yang tidak ada
                    // maka server akan terus loading, hal itu menyebabkan app akan terus meminta data
                    // sampai data yang tidak pernah ada itu terambil
                    // maka dari itu kita harus membuat count jika sudah 3 kali time out
                    // maka sudahi request ke server
                    return@takeWhile if (v is TimeoutException || v is SocketTimeoutException) {
                        if (i == 3) {
                            networkState.postValue(NetworkState.END_OF_PAGE)
                            false
                        } else {
                            i++
                            networkState.postValue(NetworkState.TIMEOUT)
                            true
                        }
                    } else {
                        networkState.postValue(NetworkState.ERROR)
                        false
                    }
                }
            }
    }

    fun getSearchManga(keyword: String): Flowable<SearchMangaResponse> {
        networkState.postValue(NetworkState.LOADING)

        return apiService.getSearchManga(keyword)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .timeout(5, TimeUnit.SECONDS)
            .retryWhen {
                it.takeWhile { v ->
                    return@takeWhile if (v is TimeoutException || v is SocketTimeoutException) {
                        networkState.postValue(NetworkState.TIMEOUT)
                        true
                    } else {
                        networkState.postValue(NetworkState.ERROR)
                        false
                    }
                }
            }
    }
}