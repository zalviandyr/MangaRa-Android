package com.zukron.mangara.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.zukron.mangara.model.DetailMangaResponse
import com.zukron.mangara.network.NetworkState
import com.zukron.mangara.repository.DetailRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable

/**
 * Project name is Manga Ra
 * Created by Zukron Alviandy R on 9/30/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private val firebaseUser = Firebase.auth.currentUser
    private val detailRepository = DetailRepository.getInstance(application)
    private val compositeDisposable = CompositeDisposable()
    private val mangaEndpoint: MutableLiveData<String> = MutableLiveData()
    private val chapterEndpoint: MutableLiveData<String> = MutableLiveData()
    val networkState: LiveData<NetworkState> = detailRepository.networkState

    val detailManga = Transformations
        .switchMap(mangaEndpoint) {
            detailRepository.getDetailManga(it, compositeDisposable)
        }

    val allChapter = Transformations
        .switchMap(mangaEndpoint) {
            detailRepository.getAllChapter(it, compositeDisposable)
        }

    val chapterImage = Transformations
        .switchMap(chapterEndpoint) {
            detailRepository.getChapterImage(it, compositeDisposable)
        }

    val favoriteMangaByEndpoint = Transformations
        .switchMap(detailManga) {
            detailRepository.getFavoriteByMangaEndpoint(firebaseUser!!, it)
        }

    val historyMangaByEndpoint = Transformations
        .switchMap(detailManga) {
            detailRepository.getHistoryByMangaEndpoint(firebaseUser!!, it)
        }

    fun setMangaEndpoint(value: String) {
        if (mangaEndpoint.value == value) {
            return
        }
        mangaEndpoint.value = value
    }

    fun setChapterEndpoint(value: String) {
        if (chapterEndpoint.value == value) {
            return
        }
        chapterEndpoint.value = value
    }

    fun getChapterEndpoint(): String = chapterEndpoint.value ?: ""

    fun setFavoriteManga(manga: DetailMangaResponse) {
        detailRepository.setFavoriteManga(firebaseUser!!, manga)
    }

    fun deleteFavoriteManga(manga: DetailMangaResponse) {
        detailRepository.deleteFavoriteManga(firebaseUser!!, manga)
    }

    fun setHistoryManga(
        manga: DetailMangaResponse,
        chapter: DetailMangaResponse.Chapter
    ) {
        detailRepository.setHistoryManga(firebaseUser!!, manga, chapter)
    }

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.clear()
    }
}