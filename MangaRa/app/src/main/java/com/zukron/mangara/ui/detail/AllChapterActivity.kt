package com.zukron.mangara.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.zukron.mangara.R
import com.zukron.mangara.adapter.AllChapterPagedListAdapter
import com.zukron.mangara.adapter.listener.OnSelectedChapterListener
import com.zukron.mangara.ui.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.activity_all_content.*

/**
 * Project name is Manga Ra
 * Created by Zukron Alviandy R on 9/30/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
class AllChapterActivity : AppCompatActivity(), OnSelectedChapterListener {
    companion object {
        const val EXTRA_MANGA_ENDPOINT = "extra_endpoint"
    }

    private var mangaEndpoint = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_content)

        mangaEndpoint = intent.getStringExtra(EXTRA_MANGA_ENDPOINT) ?: ""

        // title
        allContentAct_tvTitle.text = getString(R.string.all_chapter)

        // progress bar = tidak perlu karena app tidak akan mengambil data dari server
        // melainkan dari cache
        allContentAct_progressBar.visibility = View.GONE

        // toolbar
        allContentAct_toolbar.title = ""
        setSupportActionBar(allContentAct_toolbar)
        allContentAct_toolbar.setNavigationOnClickListener {
            finish()
        }

        // adapter
        val adapter = AllChapterPagedListAdapter()
        adapter.setOnSelectedChapterListener(this)
        allContentAct_recyclerView.adapter = adapter

        // view model
        val detailViewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        detailViewModel.setMangaEndpoint(mangaEndpoint)

        detailViewModel.allChapter.observe(this) {
            adapter.submitList(it)
        }
    }

    override fun onSelectedChapter(title: String, chapterEndpoint: String) {
        val intent = Intent(this, ChapterActivity::class.java)
        intent.putExtra(ChapterActivity.EXTRA_TITLE, title)
        intent.putExtra(ChapterActivity.EXTRA_CHAPTER_ENDPOINT, chapterEndpoint)
        intent.putExtra(ChapterActivity.EXTRA_MANGA_ENDPOINT, mangaEndpoint)

        startActivity(intent)
    }
}