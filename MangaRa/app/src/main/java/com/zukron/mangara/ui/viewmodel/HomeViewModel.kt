package com.zukron.mangara.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.zukron.mangara.network.NetworkState
import com.zukron.mangara.repository.HomeRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable

/**
 * Project name is Manga Ra
 * Created by Zukron Alviandy R on 9/28/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val compositeDisposable = CompositeDisposable()
    private val homeRepository = HomeRepository.getInstance(application)
    private val searchMangaKeyword: MutableLiveData<String> = MutableLiveData()
    val firebaseAuth = Firebase.auth
    val networkState: LiveData<NetworkState> = homeRepository.networkState

    val popularManga = homeRepository.getPopularManga(1, compositeDisposable)

    val genre = homeRepository.getGenre(compositeDisposable)

    val favoriteManga = homeRepository.getFavoriteManga(firebaseAuth.currentUser!!)

    val historyManga = homeRepository.getHistoryManga(firebaseAuth.currentUser!!)

    var searchManga = Transformations
        .switchMap(searchMangaKeyword) {
            homeRepository.getSearchManga(it, compositeDisposable)
        }

    fun setSearchMangaKeyword(keyword: String) {
        if (searchMangaKeyword.value == keyword) {
            return
        }
        searchMangaKeyword.value = keyword
    }

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.clear()
    }
}