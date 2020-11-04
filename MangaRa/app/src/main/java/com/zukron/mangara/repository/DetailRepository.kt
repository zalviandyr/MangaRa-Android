package com.zukron.mangara.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.zukron.mangara.model.ChapterMangaResponse
import com.zukron.mangara.model.DetailMangaResponse
import com.zukron.mangara.model.helper.FavoriteMangaHelper
import com.zukron.mangara.model.helper.HistoryMangaHelper
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
 * Created by Zukron Alviandy R on 9/30/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
class DetailRepository(context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: DetailRepository? = null

        fun getInstance(context: Context): DetailRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: DetailRepository(context).also { INSTANCE = it }
            }
    }

    private val apiService = RestApi.getApiService(context)

    val networkState: MutableLiveData<NetworkState> = RestApi.networkState

    fun getDetailManga(
        endpoint: String,
        compositeDisposable: CompositeDisposable
    ): LiveData<DetailMangaResponse> {
        networkState.postValue(NetworkState.LOADING)

        return object : LiveData<DetailMangaResponse>() {
            override fun onActive() {
                super.onActive()

                compositeDisposable.add(
                    apiService.getDetailManga(endpoint)
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
                        .subscribe({
                            value = it
                            networkState.postValue(NetworkState.LOADED)
                        }, {
                            networkState.postValue(NetworkState.ERROR)
                        })
                )
            }
        }
    }

    fun getAllChapter(
        endpoint: String,
        compositeDisposable: CompositeDisposable
    ): LiveData<List<DetailMangaResponse.Chapter>> {
        networkState.postValue(NetworkState.LOADED)

        return object : LiveData<List<DetailMangaResponse.Chapter>>() {
            override fun onActive() {
                super.onActive()

                compositeDisposable.add(
                    apiService.getDetailManga(endpoint)
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
                        .subscribe({
                            value = it.chapter
                            networkState.postValue(NetworkState.LOADED)
                        }, {
                            networkState.postValue(NetworkState.ERROR)
                        })
                )
            }
        }
    }

    fun getChapterImage(
        endpoint: String,
        compositeDisposable: CompositeDisposable
    ): LiveData<List<ChapterMangaResponse.ChapterImage>> {
        networkState.postValue(NetworkState.LOADING)

        return object : LiveData<List<ChapterMangaResponse.ChapterImage>>() {
            override fun onActive() {
                super.onActive()

                compositeDisposable.add(
                    apiService.getChapterImage(endpoint)
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
                        .subscribe({
                            value = it.chapterImage
                            networkState.postValue(NetworkState.LOADED)
                        }, {
                            networkState.postValue(NetworkState.ERROR)
                        })
                )
            }
        }
    }

    fun setFavoriteManga(
        firebaseUser: FirebaseUser,
        manga: DetailMangaResponse
    ) {
        val database = Firebase.database
        val ref = database.getReference("favorite").child(firebaseUser.uid)

        ref.runTransaction(object : Transaction.Handler {
            var count = 0

            override fun doTransaction(currentData: MutableData): Transaction.Result {
                if (currentData.value != null) {
                    // bisa saja terjadi null item karena user bisa menghapus item favorite
                    // dan yang seharusnya list terurut, contoh 0 1 2 3
                    // user menghapus salah satu favorite dan menjadi 0 1 3
                    // maka program akan tetap menghitung index ke 2 dan akan menyebabkan null
                    val genericTypeIndicator =
                        object : GenericTypeIndicator<List<FavoriteMangaHelper>>() {}
                    val listItem = currentData.getValue(genericTypeIndicator)!!.filterNotNull()

                    val lastIndex = listItem.lastIndex
                    val lastItem = listItem[lastIndex]

                    count = lastItem.indexData.toInt() + 1
                }

                return Transaction.success(currentData)
            }

            override fun onComplete(
                error: DatabaseError?,
                committed: Boolean,
                currentData: DataSnapshot?
            ) {
                if (committed) {
                    val favoriteMangaHelper = FavoriteMangaHelper(
                        count.toString(), manga.title, manga.thumb, manga.type, manga.mangaEndpoint
                    )

                    val refTemp = ref.child(count.toString())
                    refTemp.setValue(favoriteMangaHelper)
                }
            }
        })
    }

    fun getFavoriteByMangaEndpoint(
        firebaseUser: FirebaseUser,
        manga: DetailMangaResponse
    ): LiveData<Boolean> {
        return object : LiveData<Boolean>() {
            override fun onActive() {
                super.onActive()

                val database = Firebase.database
                val ref = database.getReference("favorite")

                val query = ref.orderByKey().equalTo(firebaseUser.uid)
                query.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.child(firebaseUser.uid).exists()) {
                            val genericTypeIndicator =
                                object : GenericTypeIndicator<List<FavoriteMangaHelper>>() {}
                            val results = snapshot.child(firebaseUser.uid)
                                .getValue(genericTypeIndicator)!!.filterNotNull()

                            for (result in results) {
                                if (result.endpoint == manga.mangaEndpoint) {
                                    value = result.endpoint == manga.mangaEndpoint
                                    break
                                }
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

    fun deleteFavoriteManga(
        firebaseUser: FirebaseUser,
        manga: DetailMangaResponse
    ) {
        val database = Firebase.database
        val ref = database.getReference("favorite").child(firebaseUser.uid)

        val query = ref.orderByChild("endpoint").equalTo(manga.mangaEndpoint)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (itemSnapshot in snapshot.children) {
                    itemSnapshot.ref.removeValue()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // who care
            }
        })
    }

    fun setHistoryManga(
        firebaseUser: FirebaseUser,
        manga: DetailMangaResponse,
        chapter: DetailMangaResponse.Chapter
    ) {
        val database = Firebase.database
        val ref = database.getReference("history").child(firebaseUser.uid)

        ref.runTransaction(object : Transaction.Handler {
            var count = 0

            override fun doTransaction(currentData: MutableData): Transaction.Result {
                if (currentData.value != null) {
                    val genericTypeIndicator =
                        object : GenericTypeIndicator<List<HistoryMangaHelper>>() {}
                    val listItem = currentData.getValue(genericTypeIndicator)!!

                    val lastIndex = listItem.lastIndex
                    val lastItem = listItem[lastIndex]

                    count = lastItem.indexData.toInt() + 1
                }

                return Transaction.success(currentData)
            }

            override fun onComplete(
                error: DatabaseError?,
                committed: Boolean,
                currentData: DataSnapshot?
            ) {
                // menghindari data dengan endpoint chapter yang duplikat
                if (committed) {
                    val genericTypeIndicator =
                        object : GenericTypeIndicator<List<HistoryMangaHelper>>() {}

                    val dataList = currentData?.getValue(genericTypeIndicator)

                    val isExist = dataList?.find {
                        it.mangaEndpoint == manga.mangaEndpoint
                    }

                    // set count menjadi index data yang duplikat
                    val count = isExist?.indexData?.toInt() ?: count

                    val historyMangaHelper = HistoryMangaHelper(
                        count.toString(),
                        manga.title,
                        manga.thumb,
                        manga.type,
                        chapter.chapterTitle,
                        chapter.chapterEndpoint,
                        manga.mangaEndpoint
                    )

                    val refTemp = ref.child(count.toString())
                    refTemp.setValue(historyMangaHelper)
                }
            }
        })
    }

    fun getHistoryByMangaEndpoint(
        firebaseUser: FirebaseUser,
        manga: DetailMangaResponse
    ): LiveData<HistoryMangaHelper> {
        return object : LiveData<HistoryMangaHelper>() {
            override fun onActive() {
                super.onActive()

                val database = Firebase.database
                val ref = database.getReference("history")

                val query = ref.orderByKey().equalTo(firebaseUser.uid)
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val genericTypeIndicator =
                            object : GenericTypeIndicator<List<HistoryMangaHelper>>() {}
                        val results = snapshot.child(firebaseUser.uid)
                            .getValue(genericTypeIndicator)

                        val result = results?.find {
                            it.mangaEndpoint == manga.mangaEndpoint
                        }
                        value = result
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // who care
                    }

                })
            }
        }
    }
}