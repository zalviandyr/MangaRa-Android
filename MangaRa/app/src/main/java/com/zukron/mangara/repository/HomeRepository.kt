package com.zukron.mangara.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.zukron.mangara.model.DetailMangaResponse
import com.zukron.mangara.model.GenreResponse
import com.zukron.mangara.model.PopularMangaResponse
import com.zukron.mangara.network.NetworkState
import com.zukron.mangara.network.RestApi
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * Project name is Manga Ra
 * Created by Zukron Alviandy R on 9/28/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
class HomeRepository(context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: HomeRepository? = null

        fun getInstance(context: Context): HomeRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: HomeRepository(context).also { INSTANCE = it }
            }
    }

    private var apiService = RestApi.getApiService(context)
    val networkState: MutableLiveData<NetworkState> = RestApi.networkState

    fun getPopularManga(
        page: Int,
        compositeDisposable: CompositeDisposable
    ): LiveData<List<PopularMangaResponse.Manga>> {
        networkState.postValue(NetworkState.LOADING)

        var i = 0
        return object : LiveData<List<PopularMangaResponse.Manga>>() {
            override fun onActive() {
                super.onActive()

                compositeDisposable.add(
                    apiService.getPopularManga(page)
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
                                return@takeWhile if (v is SocketTimeoutException || v is TimeoutException) {
                                    if (i == 3) {
                                        networkState.postValue(NetworkState.ERROR)
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
                        .subscribe({
                            value = it.data
                            networkState.postValue(NetworkState.LOADED)
                        }, {
                            networkState.postValue(NetworkState.ERROR)
                        })
                )
            }
        }
    }

    fun getGenre(
        compositeDisposable: CompositeDisposable
    ): LiveData<List<GenreResponse.Genre>> {
        networkState.postValue(NetworkState.LOADING)

        return object : LiveData<List<GenreResponse.Genre>>() {
            override fun onActive() {
                super.onActive()

                compositeDisposable.add(
                    apiService.getGenre()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .timeout(5, TimeUnit.SECONDS)
                        .retryWhen {
                            it.takeWhile { v ->
                                return@takeWhile if (v is SocketTimeoutException || v is TimeoutException) {
                                    networkState.postValue(NetworkState.TIMEOUT)
                                    true
                                } else {
                                    networkState.postValue(NetworkState.ERROR)
                                    false
                                }
                            }
                        }
                        .subscribe({
                            value = it.data
                            networkState.postValue(NetworkState.LOADED)
                        }, {
                            networkState.postValue(NetworkState.ERROR)
                        })
                )
            }
        }
    }

    fun getFavoriteManga(firebaseUser: FirebaseUser): LiveData<List<DetailMangaResponse>> {
        return object : LiveData<List<DetailMangaResponse>>() {
            override fun onActive() {
                super.onActive()

                val database = Firebase.database
                val ref = database.getReference("favorite")

                val query = ref.orderByKey().equalTo(firebaseUser.uid)
                query.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.child(firebaseUser.uid).exists()) {
                            @Suppress("UNCHECKED_CAST")
                            val results = snapshot.child(firebaseUser.uid)
                                .value as List<Map<String, String>>

                            // bisa saja terjadi null item karena user bisa menghapus item favorite
                            // dan yang seharusnya list terurut, contoh 0 1 2 3
                            // user menghapus salah satu favorite dan menjadi 0 1 3
                            // maka program akan tetap menghitung index ke 2 dan akan menyebabkan null
                            val resultList = mutableListOf<DetailMangaResponse>()

                            // jika tidak memiliki size 1 karena index pertama adalah inisialisasi
                            // yang di buat pada sign up
                            if (results.size != 1) {
                                val trimResult = results.subList(1, results.size).filterNotNull()
                                for (result in trimResult) {
                                    val detailManga = DetailMangaResponse(
                                        "",
                                        listOf(),
                                        listOf(),
                                        result["endpoint"].toString(),
                                        "",
                                        "",
                                        result["thumb"].toString(),
                                        result["title"].toString(),
                                        result["type"].toString(),
                                    )

                                    resultList.add(detailManga)
                                }

                                value = resultList
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // who care
                    }
                })
            }
        }
    }

    fun getHistoryManga(firebaseUser: FirebaseUser)
            : LiveData<List<Map<String, String>>> {
        return object : LiveData<List<Map<String, String>>>() {
            override fun onActive() {
                super.onActive()

                val database = Firebase.database
                val ref = database.getReference("history")

                val query = ref.orderByKey().equalTo(firebaseUser.uid)
                query.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.child(firebaseUser.uid).exists()) {
                            @Suppress("UNCHECKED_CAST")
                            val result = snapshot.child(firebaseUser.uid)
                                .value as List<Map<String, String>>

                            // jika tidak memiliki size 1 karena index pertama adalah inisialisasi
                            // yang di buat pada sign up
                            value = result.subList(1, result.size)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // who care
                    }
                })
            }
        }
    }
}