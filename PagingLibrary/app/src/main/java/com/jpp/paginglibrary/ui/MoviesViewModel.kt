package com.jpp.paginglibrary.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.jpp.paginglibrary.datalayer.DataMovie
import java.util.concurrent.Executors

/**
 * A simple ViewModel implementation for the Activity.
 * The heavy lifting to fetch the movies is done by the loading page.
 */
class MoviesViewModel : ViewModel() {


    private lateinit var viewState: LiveData<MoviesViewState>

    private val pagedList by lazy {
        val factory = MoviesDataSourceFactory()


        /*
         * Map the internal state of the MoviesDataSource to a LiveData that the
         * UI can understand.
         */
        viewState = Transformations.switchMap(factory.mutableLiveData) {
            it.loadingWhenPages
        }

        val config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(2) // 2 pre-loads now
                .build()

        LivePagedListBuilder(factory, config)
                .setFetchExecutor(Executors.newFixedThreadPool(5))
                .build()
    }



    fun getViewState(): LiveData<MoviesViewState> = viewState
    fun getMovieList(): LiveData<PagedList<DataMovie>> = pagedList
}