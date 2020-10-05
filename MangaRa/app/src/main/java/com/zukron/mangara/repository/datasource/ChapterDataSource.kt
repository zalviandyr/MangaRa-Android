package com.zukron.mangara.repository.datasource

import androidx.paging.PositionalDataSource
import com.zukron.mangara.model.DetailMangaResponse
import com.zukron.mangara.repository.AllContentRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable

/**
 * Project name is Manga Ra
 * Created by Zukron Alviandy R on 9/29/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
class ChapterDataSource(
    private val endpoint: String,
    private val allContentRepository: AllContentRepository,
    private val compositeDisposable: CompositeDisposable
) : PositionalDataSource<DetailMangaResponse.Chapter>() {
    private var totalCount: Int = 0

    private fun fetchData(
        starPosition: Int,
        loadCount: Int,
        load: (List<DetailMangaResponse.Chapter>) -> Unit
    ) {
        compositeDisposable.add(
            allContentRepository.getAllChapter(endpoint)
                .map {
                    // mengatasi data yang tidak kelipatan postPerPage
                    // contoh banyak data 90, maka semisal startPosition ada di 80
                    // maka 80 - 90 tidak diambil melainkan data yang diambil adalah 80 - 100
                    // karena postPerPage ny adalah 20
                    var endPosition = starPosition + loadCount
                    if (endPosition > totalCount) {
                        val remainsPosition = totalCount - starPosition
                        endPosition = starPosition + remainsPosition
                    }

                    it.chapter.subList(starPosition, endPosition)
                }
                .subscribe {
                    load(it)
                }
        )
    }

    override fun loadInitial(
        params: LoadInitialParams,
        callback: LoadInitialCallback<DetailMangaResponse.Chapter>
    ) {
        compositeDisposable.add(
            allContentRepository.getAllChapter(endpoint)
                .subscribe {
                    totalCount = it.chapter.size
                    val position = computeInitialLoadPosition(params, totalCount)
                    val loadCount = computeInitialLoadSize(params, position, totalCount)

                    fetchData(position, loadCount) { list ->
                        callback.onResult(list, 0, totalCount)
                    }
                }
        )
    }

    override fun loadRange(
        params: LoadRangeParams,
        callback: LoadRangeCallback<DetailMangaResponse.Chapter>
    ) {
        fetchData(params.startPosition, params.loadSize) { list ->
            callback.onResult(list)
        }
    }
}