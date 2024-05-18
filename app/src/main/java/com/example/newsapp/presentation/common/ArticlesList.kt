package com.example.newsapp.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.newsapp.domain.model.Article
import com.example.newsapp.presentation.Dimens.MediumPadding1


@Composable
fun ArticlesList(
    modifier: Modifier = Modifier,
    articles: LazyPagingItems<Article>,
    onCLick: (Article) -> Unit
) {

    val hundlePagingResult = hundlePagingResult(articles = articles)


    if (hundlePagingResult) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(MediumPadding1),
            contentPadding = PaddingValues(MediumPadding1)
        ) {
            items(articles.itemCount) {
                articles[it]?.let { article ->
                    ArticleCard(article = article) {
                        onCLick(article)
                    }
                }
            }
        }
    }


}

@Composable
fun ArticlesList(
    modifier: Modifier = Modifier,
    articles: List<Article>,
    onCLick: (Article) -> Unit
) {

    if (articles.isEmpty()) {
        EmptyScreen()
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(MediumPadding1),
        ) {
            items(articles.size) {
                val article = articles[it]
                ArticleCard(article = article) {
                    onCLick(article)
                }


            }

        }
    }


}

@Composable
fun ArticlesList(
    modifier: Modifier = Modifier,
    articles: LazyPagingItems<Article>,
    onCLick: (Article) -> Unit,
    onRefresh: () -> Unit
) {
    val isRefresh = remember {
        mutableStateOf(false)
    }
    hundlePullRefresh(articles = articles, isRefresh = isRefresh)

    PullToRefreshLazyColumn(
        modifier = modifier,
        items = articles.itemSnapshotList.items,
        content = {
            ArticleCard(article = it) {
                onCLick(it)
            }
        },
        isRefreshing = isRefresh.value
    ) {


        onRefresh()

    }

}

@Composable
fun hundlePullRefresh(
    articles: LazyPagingItems<Article>,
    isRefresh: MutableState<Boolean>
): Boolean {
    val loadState = articles.loadState


    val error = when {
        loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
        loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
        loadState.append is LoadState.Error -> loadState.append as LoadState.Error
        else -> null
    }



    return when {
        loadState.refresh is LoadState.Loading -> {
            isRefresh.value = true
            ShimmerEffect()
            false

        }

        error != null -> {
            isRefresh.value = false
            EmptyScreen(error = error)
            false
        }

        articles.itemCount == 0 -> {
            isRefresh.value = false
            EmptyScreen()
            false
        }

        else -> {
            isRefresh.value = false
            true
        }
    }


}

@Composable
fun hundlePagingResult(
    articles: LazyPagingItems<Article>,
): Boolean {
    val loadState = articles.loadState


    val error = when {
        loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
        loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
        loadState.append is LoadState.Error -> loadState.append as LoadState.Error
        else -> null
    }

    return when {
        loadState.refresh is LoadState.Loading -> {
            ShimmerEffect()
            false

        }

        error != null -> {
            EmptyScreen(error = error)
            false
        }

        articles.itemCount == 0 -> {
            EmptyScreen()
            false
        }

        else -> {
            true
        }
    }


}

@Composable
private fun ShimmerEffect() {
    Column(verticalArrangement = Arrangement.spacedBy(MediumPadding1)) {

        repeat(10) {
            ArticleCardShimmerEffect(
                modifier = Modifier.padding(horizontal = MediumPadding1)
            )
        }

    }
}