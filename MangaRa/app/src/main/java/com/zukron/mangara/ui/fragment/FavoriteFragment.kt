package com.zukron.mangara.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.zukron.mangara.R
import com.zukron.mangara.adapter.FavoriteAdapter
import com.zukron.mangara.adapter.listener.OnSelectedMangaListener
import com.zukron.mangara.ui.detail.DetailMangaActivity
import com.zukron.mangara.ui.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_favorite.*

/**
 * Project name is Manga Ra
 * Created by Zukron Alviandy R on 9/28/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
class FavoriteFragment : Fragment(), OnSelectedMangaListener {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // view model
        val homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)

        homeViewModel.favoriteManga.observe(requireActivity()) {
            // adapter
            val adapter = FavoriteAdapter(it)
            adapter.setOnSelectedMangaListener(this)
            favoriteFrag_recyclerView.adapter = adapter
        }
    }

    override fun onSelectedManga(endpoint: String) {
        val intent = Intent(requireContext(), DetailMangaActivity::class.java)
        intent.putExtra(DetailMangaActivity.EXTRA_MANGA_ENDPOINT, endpoint)
        startActivity(intent)
    }
}