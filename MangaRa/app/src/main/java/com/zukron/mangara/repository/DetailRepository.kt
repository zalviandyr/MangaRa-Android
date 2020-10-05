package com.zukron.mangara.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.zukron.mangara.model.ChapterMangaResponse
import com.zukron.mangara.model.DetailMangaResponse
import com.zukron.mangara.network.NetworkState
import com.zukron.mangara.network.RestApi
import com.zukron.mangara.repository.factory.ChapterDataFactory
import com.zukron.mangara.repository.factory.ChapterImageDataFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.net.SocketTimeoutException
import java.util.concurrent.Executors
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
    private val allContentRepository = AllContentRepository.getInstance(context)
    private val postPerPage = 20
    private val executor = Executors.newFixedThreadPool(5)
    private val pageListConfig = PagedList.Config.Builder()
        .setPageSize(postPerPage)
        .setEnablePlaceholders(false)
        .build()

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
    ): LiveData<PagedList<DetailMangaResponse.Chapter>> {
        val chapterDataFactory =
            ChapterDataFactory(endpoint, allContentRepository, compositeDisposable)

        return LivePagedListBuilder(chapterDataFactory, pageListConfig)
            .setFetchExecutor(executor)
            .build()
    }

    fun getChapterImage(
        endpoint: String,
        compositeDisposable: CompositeDisposable
    ): LiveData<PagedList<ChapterMangaResponse.ChapterImage>> {
        val chapterImageDataFactory =
            ChapterImageDataFactory(endpoint, allContentRepository, compositeDisposable)

        return LivePagedListBuilder(chapterImageDataFactory, pageListConfig)
            .setFetchExecutor(executor)
            .build()
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
                    val listItem = currentData.value as List<*>
                    val listItemNotNull = listItem.filterNotNull()
                    val lastIndex = listItemNotNull.lastIndex

                    @Suppress("UNCHECKED_CAST")
                    val map = listItemNotNull[lastIndex] as Map<String, String>
                    count = (map["indexData"] ?: error("")).toInt() + 1
                }

                return Transaction.success(currentData)
            }

            override fun onComplete(
                error: DatabaseError?,
                committed: Boolean,
                currentData: DataSnapshot?
            ) {
                if (committed) {
                    val refTemp = ref.child(count.toString())
                    refTemp.child("indexData").setValue(count.toString())
                    refTemp.child("thumb").setValue(manga.thumb)
                    refTemp.child("title").setValue(manga.title)
                    refTemp.child("endpoint").setValue(manga.mangaEndpoint)
                    refTemp.child("type").setValue(manga.type)
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
                            @Suppress("UNCHECKED_CAST")
                            val results = snapshot.child(firebaseUser.uid)
                                .value as List<Map<String, String>>

                            for (result in results.filterNotNull()) {
                                if (result["endpoint"] == manga.mangaEndpoint) {
                                    value = result["endpoint"] == manga.mangaEndpoint
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
                val TAG = "DetailRepository"

                Log.d(TAG, "onDataChange: ${snapshot.children}")
                Log.d(TAG, "onDataChange: ${snapshot.value}")

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
                    val listItem = currentData.value as List<*>
                    val lastIndex = listItem.lastIndex

                    @Suppress("UNCHECKED_CAST")
                    val map = listItem[lastIndex] as Map<String, String>
                    count = (map["indexData"] ?: error("")).toInt() + 1
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
                    @Suppress("UNCHECKED_CAST")
                    val dataList = currentData?.value as List<Map<String, String>>

                    val isExist = dataList.find {
                        it["mangaEndpoint"] == manga.mangaEndpoint
                    }
                    val count = if (isExist != null) {
                        isExist["indexData"]?.toInt()
                    } else {
                        count
                    }

                    val refTemp = ref.child(count.toString())
                    refTemp.child("indexData").setValue(count.toString())
                    refTemp.child("title").setValue(manga.title)
                    refTemp.child("thumb").setValue(manga.thumb)
                    refTemp.child("type").setValue(manga.type)
                    refTemp.child("mangaEndpoint").setValue(manga.mangaEndpoint)
                    refTemp.child("chapter").setValue(chapter.chapterTitle)
                    refTemp.child("lastChapter").setValue(chapter.chapterEndpoint)
                }
            }
        })
    }

    fun getHistoryByMangaEndpoint(
        firebaseUser: FirebaseUser,
        manga: DetailMangaResponse
    ): LiveData<Map<String, String>?> {
        return object : LiveData<Map<String, String>?>() {
            override fun onActive() {
                super.onActive()

                val database = Firebase.database
                val ref = database.getReference("history")

                val query = ref.orderByKey().equalTo(firebaseUser.uid)
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        @Suppress("UNCHECKED_CAST")
                        val results = snapshot.child(firebaseUser.uid)
                            .value as List<Map<String, String>>

                        val result = results.find {
                            it["mangaEndpoint"] == manga.mangaEndpoint
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