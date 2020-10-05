package com.zukron.mangara.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.zukron.mangara.R
import com.zukron.mangara.adapter.GenreAdapter
import com.zukron.mangara.adapter.PopularAdapter
import com.zukron.mangara.adapter.listener.OnSelectedGenreListener
import com.zukron.mangara.adapter.listener.OnSelectedMangaListener
import com.zukron.mangara.model.GenreResponse
import com.zukron.mangara.network.NetworkState
import com.zukron.mangara.tools.Utilities
import com.zukron.mangara.ui.home.AllContentActivity
import com.zukron.mangara.ui.detail.DetailMangaActivity
import com.zukron.mangara.ui.home.SearchMangaActivity
import com.zukron.mangara.ui.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment(), View.OnClickListener, OnSelectedGenreListener,
    OnSelectedMangaListener {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    private lateinit var genreAdapter: GenreAdapter<GenreResponse.Genre>
    private lateinit var popularAdapter: PopularAdapter
    private lateinit var homeViewModel: HomeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // adapter
        genreAdapter = GenreAdapter()
        popularAdapter = PopularAdapter()
        genreAdapter.setOnSelectedGenreListener(this)
        popularAdapter.setOnSelectedMangaListener(this)

        // view model
        homeViewModel = requireActivity().let {
            ViewModelProvider(it).get(HomeViewModel::class.java)
        }

        // set name
        homeFrag_tvFullName.text = homeViewModel.firebaseAuth.currentUser?.displayName
        homeFrag_tvGreet.text = Utilities.greeting(requireContext())

        homeViewModel.popularManga.observe(requireActivity()) {
            popularAdapter.popularMangaResponseList = it
            view.homeFrag_rvPopular.adapter = popularAdapter
        }

        homeViewModel.genre.observe(requireActivity()) {
            genreAdapter.genreList = it.subList(0, 9)
            view.homeFrag_rvGenre.adapter = genreAdapter
        }

        homeViewModel.networkState.observe(requireActivity()) {
            if (it == NetworkState.LOADED) {
                view.homeFrag_pbPopular.visibility = View.GONE
                view.homeFrag_pbGenre.visibility = View.GONE
            }

            if (it == NetworkState.NO_CONNECTION || it == NetworkState.TIMEOUT) {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
            }
        }

        // button listener
        homeFrag_btnMorePopular.setOnClickListener(this)
        homeFrag_btnMoreGenre.setOnClickListener(this)
        homeFrag_btnSearch.setOnClickListener(this)

        // country action (linear layout listener)
        homeFrag_llManga.setOnClickListener(this)
        homeFrag_llManhua.setOnClickListener(this)
        homeFrag_llManhwa.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        view?.let {
            when (it.id) {
                R.id.homeFrag_btnMorePopular -> {
                    val intent = Intent(requireContext(), AllContentActivity::class.java)
                    intent.putExtra(
                        AllContentActivity.EXTRA_TYPE,
                        AllContentActivity.Type.ALL_POPULAR
                    )
                    startActivity(intent)
                }
                R.id.homeFrag_btnMoreGenre -> {
                    if (homeFrag_btnMoreGenre.isChecked) {
                        // total semua genre ada 63, data yng kita perlukan hanya 56
                        // kenapa karena 7 endpoint lainnya bukan endpoint melainkan halaman website
                        genreAdapter.genreList = homeViewModel.genre.value?.subList(0, 56)
                    } else {
                        genreAdapter.genreList = homeViewModel.genre.value?.subList(0, 9)
                    }
                }
                R.id.homeFrag_btnSearch -> {
                    val intent = Intent(requireContext(), SearchMangaActivity::class.java)
                    startActivity(intent)
                }
                R.id.homeFrag_llManga -> {
                    val intent = Intent(requireContext(), AllContentActivity::class.java)
                    intent.putExtra(
                        AllContentActivity.EXTRA_TYPE,
                        AllContentActivity.Type.ALL_MANGA
                    )
                    startActivity(intent)
                }
                R.id.homeFrag_llManhua -> {
                    val intent = Intent(requireContext(), AllContentActivity::class.java)
                    intent.putExtra(
                        AllContentActivity.EXTRA_TYPE,
                        AllContentActivity.Type.ALL_MANHUA
                    )
                    startActivity(intent)
                }
                R.id.homeFrag_llManhwa -> {
                    val intent = Intent(requireContext(), AllContentActivity::class.java)
                    intent.putExtra(
                        AllContentActivity.EXTRA_TYPE,
                        AllContentActivity.Type.ALL_MANHWA
                    )
                    startActivity(intent)
                }
            }
        }
    }

    override fun onSelectedGenre(genre: String, genreEndpoint: String) {
        val intent = Intent(requireContext(), AllContentActivity::class.java)
        intent.putExtra(AllContentActivity.EXTRA_GENRE, genre)
        intent.putExtra(AllContentActivity.EXTRA_GENRE_ENDPOINT, genreEndpoint)
        startActivity(intent)
    }

    override fun onSelectedManga(endpoint: String) {
        val intent = Intent(requireContext(), DetailMangaActivity::class.java)
        intent.putExtra(DetailMangaActivity.EXTRA_MANGA_ENDPOINT, endpoint)
        startActivity(intent)
    }
}