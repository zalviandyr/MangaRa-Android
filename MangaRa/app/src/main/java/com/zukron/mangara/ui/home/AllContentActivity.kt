package com.zukron.mangara.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.zukron.mangara.R
import com.zukron.mangara.adapter.AllContentPagedListAdapter
import com.zukron.mangara.adapter.listener.OnSelectedMangaListener
import com.zukron.mangara.model.*
import com.zukron.mangara.network.NetworkState
import com.zukron.mangara.ui.detail.DetailMangaActivity
import com.zukron.mangara.ui.viewmodel.AllContentViewModel
import kotlinx.android.synthetic.main.activity_all_content.*

/**
 * Project name is Manga Ra
 * Created by Zukron Alviandy R on 9/29/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
class AllContentActivity
    : AppCompatActivity(), OnSelectedMangaListener {
    object Type {
        const val ALL_POPULAR = "All Popular"
        const val ALL_MANGA = "All Manga"
        const val ALL_MANHUA = "All Manhua"
        const val ALL_MANHWA = "All Manhwa"
    }

    companion object {
        const val EXTRA_TYPE = "extra_type"
        const val EXTRA_GENRE = "extra_genre"
        const val EXTRA_GENRE_ENDPOINT = "extra_genre_endpoint"
    }

    private lateinit var allContentViewModel: AllContentViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_content)

        // title and genre endpoint
        var endpoint = ""
        val title = if (intent.getStringExtra(EXTRA_TYPE) != null) {
            intent.getStringExtra(EXTRA_TYPE)
        } else {
            endpoint = intent.getStringExtra(EXTRA_GENRE_ENDPOINT) ?: ""
            intent.getStringExtra(EXTRA_GENRE)
        }
        allContentAct_tvTitle.text = title

        // toolbar
        allContentAct_toolbar.title = ""
        setSupportActionBar(allContentAct_toolbar)
        allContentAct_toolbar.setNavigationOnClickListener {
            finish()
        }

        // adapter
        val contentAdapter =
            when (title) {
                Type.ALL_POPULAR ->
                    AllContentPagedListAdapter<PopularMangaResponse.Manga>(this)
                Type.ALL_MANGA, Type.ALL_MANHUA, Type.ALL_MANHWA ->
                    AllContentPagedListAdapter<MangaResponse.Manga>(this)
                else ->
                    AllContentPagedListAdapter<GenreMangaResponse.Manga>(this)
            }
        allContentAct_recyclerView.adapter = contentAdapter

        // view model
        allContentViewModel = ViewModelProvider(this).get(AllContentViewModel::class.java)
        allContentViewModel.setType(title!!)

        @Suppress("UNCHECKED_CAST")
        when (title) {
            Type.ALL_POPULAR -> {
                allContentViewModel.popularManga.observe(this) {
                    (contentAdapter as AllContentPagedListAdapter<PopularMangaResponse.Manga>)
                        .submitList(it)
                }
            }
            Type.ALL_MANGA -> {
                allContentViewModel.manga.observe(this) {
                    (contentAdapter as AllContentPagedListAdapter<MangaResponse.Manga>)
                        .submitList(it)
                }
            }
            Type.ALL_MANHUA -> {
                allContentViewModel.manhua.observe(this) {
                    (contentAdapter as AllContentPagedListAdapter<MangaResponse.Manga>)
                        .submitList(it)
                }
            }
            Type.ALL_MANHWA -> {
                allContentViewModel.manhwa.observe(this) {
                    (contentAdapter as AllContentPagedListAdapter<MangaResponse.Manga>)
                        .submitList(it)
                }
            }
            else -> {
                allContentViewModel.setGenreEndpoint(endpoint)
                allContentViewModel.genreManga.observe(this) {
                    (contentAdapter as AllContentPagedListAdapter<GenreMangaResponse.Manga>)
                        .submitList(it)
                }
            }
        }

        allContentViewModel.networkState.observe(this) {
            if (!allContentViewModel.isListEmpty()) {
                contentAdapter.setNetworkState(it)
            }

            if (it == NetworkState.LOADED) {
                allContentAct_progressBar.visibility = View.GONE
            }

            if (it == NetworkState.END_OF_PAGE || it == NetworkState.NO_CONNECTION || it == NetworkState.TIMEOUT) {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onSelectedManga(endpoint: String) {
        val intent = Intent(this, DetailMangaActivity::class.java)
        intent.putExtra(DetailMangaActivity.EXTRA_MANGA_ENDPOINT, endpoint)
        startActivity(intent)
    }
}