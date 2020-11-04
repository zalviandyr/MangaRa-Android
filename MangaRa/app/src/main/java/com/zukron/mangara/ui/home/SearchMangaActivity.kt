package com.zukron.mangara.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModelProvider
import com.zukron.mangara.R
import com.zukron.mangara.adapter.SearchMangaAdapter
import com.zukron.mangara.adapter.listener.OnSelectedMangaListener
import com.zukron.mangara.network.NetworkState
import com.zukron.mangara.ui.detail.DetailMangaActivity
import com.zukron.mangara.ui.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.activity_search_manga.*

class SearchMangaActivity
    : AppCompatActivity(), OnSelectedMangaListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_manga)

        // progress bar
        searchMangaAct_progressBar.visibility = View.GONE

        // toolbar
        searchMangaAct_toolbar.title = ""
        setSupportActionBar(searchMangaAct_toolbar)
        searchMangaAct_toolbar.setNavigationOnClickListener {
            finish()
        }

        // view model
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        // search
        searchMangaAct_inputSearch.editText?.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH || keyEvent.action == KeyEvent.KEYCODE_ENTER) {
                homeViewModel.setSearchMangaKeyword(textView.text.toString())
                searchMangaAct_progressBar.visibility = View.VISIBLE
                true
            } else {
                false
            }
        }

        homeViewModel.searchManga.observe(this) {
            // adapter
            val adapter = SearchMangaAdapter(it, this)
            searchMangaAct_recyclerView.adapter = adapter
        }

        homeViewModel.networkState.observe(this) {
            if (it == NetworkState.LOADED) {
                searchMangaAct_progressBar.visibility = View.GONE
            }
        }
    }

    override fun onSelectedManga(endpoint: String) {
        val intent = Intent(this, DetailMangaActivity::class.java)
        intent.putExtra(DetailMangaActivity.EXTRA_MANGA_ENDPOINT, endpoint)
        startActivity(intent)
    }
}