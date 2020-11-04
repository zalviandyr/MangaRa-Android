package com.zukron.mangara.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.zukron.mangara.R
import com.zukron.mangara.adapter.HistoryAdapter
import com.zukron.mangara.adapter.listener.OnSelectedHistoryListener
import com.zukron.mangara.model.helper.HistoryMangaHelper
import com.zukron.mangara.ui.detail.ChapterActivity
import com.zukron.mangara.ui.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_history.view.*

/**
 * Project name is Manga Ra
 * Created by Zukron Alviandy R on 9/28/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
class HistoryFragment : Fragment(), OnSelectedHistoryListener {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // view model
        val homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)

        homeViewModel.historyManga.observe(requireActivity()) {
            val adapter = HistoryAdapter(it, this)
            view.historyFrag_recyclerView.adapter = adapter
        }
    }

    override fun onSelectedHistory(history: HistoryMangaHelper) {
        val intent = Intent(requireContext(), ChapterActivity::class.java)
        intent.putExtra(ChapterActivity.EXTRA_TITLE, history.chapter)
        intent.putExtra(ChapterActivity.EXTRA_CHAPTER_ENDPOINT, history.lastChapter)
        intent.putExtra(ChapterActivity.EXTRA_MANGA_ENDPOINT, history.mangaEndpoint)

        startActivity(intent)
    }
}