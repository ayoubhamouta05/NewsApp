package com.example.newsapp.presentation.search

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.newsapp.presentation.Dimens.MediumPadding1
import com.example.newsapp.presentation.common.ArticlesList
import com.example.newsapp.presentation.common.SearchBar
import com.example.newsapp.presentation.nvgraph.Route

@Composable
fun SearchScreen(
    state: SearchState,
    event: (SearchEvent) -> Unit,
    navigate: (String) -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(
                top = MediumPadding1,
            )
            .statusBarsPadding()
    ) {
        SearchBar(
            modifier = Modifier.padding(horizontal = MediumPadding1),
            text = state.searchQuery,
            readOnly = false,
            onValueChanged = { event(SearchEvent.UpdateSearchQuery(it)) },
            onSearch = { event(SearchEvent.SearchNews) }
        )

        Spacer(modifier = Modifier.height(MediumPadding1))

        Log.d("SearchScreen" , state.articles.toString())
        state.articles?.let {
            val articles = it.collectAsLazyPagingItems()
            Log.d("SearchScreen" , articles.itemCount.toString())
            ArticlesList(articles = articles, onCLick = { navigate(Route.DetailsScreen.route) })
        }


    }

}