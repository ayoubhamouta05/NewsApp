package com.example.newsapp.presentation.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.usecases.news.NewsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val newsUseCases: NewsUseCases
) : ViewModel() {

    var sideEffect by mutableStateOf<String?>(null)
        private set
    var isSaved by mutableStateOf(false)
        private set

    private suspend fun upsertArticle(article: Article) {
        newsUseCases.upsertArticle(article)
        sideEffect = "Article Saved"
        isSaved = true
    }

    private suspend fun deleteArticle(article: Article) {
        newsUseCases.deleteArticle(article)
        sideEffect = "Article Deleted"
        isSaved = false
    }
    fun checkExistence(article: Article) {
        newsUseCases.selectArticles().onEach {
            if (it.contains(article)){
                isSaved = true
                return@onEach
            }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: DetailsEvent){
        when(event){
            is DetailsEvent.UpsertDeleteArticle ->{
                viewModelScope.launch {
                    val article = newsUseCases.selectArticle(event.article.url)
                    if(article == null){
                        upsertArticle(event.article)
                    }else{
                        deleteArticle(event.article)
                    }
                }

            }
            is DetailsEvent.RemoveSideEffect ->{
                sideEffect = null
            }
        }

    }


}