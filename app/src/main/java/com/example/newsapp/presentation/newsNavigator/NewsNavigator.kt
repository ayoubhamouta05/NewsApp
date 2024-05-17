package com.example.newsapp.presentation.newsNavigator

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.newsapp.R
import com.example.newsapp.domain.model.Article
import com.example.newsapp.presentation.bookmark.BookMarkScreen
import com.example.newsapp.presentation.bookmark.BookMarkViewModel
import com.example.newsapp.presentation.details.DetailsEvent
import com.example.newsapp.presentation.details.DetailsScreen
import com.example.newsapp.presentation.details.DetailsViewModel
import com.example.newsapp.presentation.home.HomeScreen
import com.example.newsapp.presentation.home.HomeViewModel
import com.example.newsapp.presentation.newsNavigator.components.BottomNavigationItem
import com.example.newsapp.presentation.newsNavigator.components.NewsBottomNavigation
import com.example.newsapp.presentation.nvgraph.Route
import com.example.newsapp.presentation.search.SearchScreen
import com.example.newsapp.presentation.search.SearchViewModel

@Composable
fun NewsNavigator() {
    val bottomNavigationItem = remember {

        listOf(
            BottomNavigationItem(icon = R.drawable.ic_home, text = "Home"),
            BottomNavigationItem(icon = R.drawable.ic_search, text = "Search"),
            BottomNavigationItem(icon = R.drawable.ic_bookmark, text = "Bookmark"),
        )
    }


    val navController = rememberNavController()

    val backStackState = navController.currentBackStackEntryAsState().value

    var selectedItem by rememberSaveable {
        mutableIntStateOf(0)
    }



    selectedItem = remember (key1 = backStackState){
        when (backStackState?.destination?.route) {
            Route.HomeScreen.route -> 0
            Route.SearchScreen.route -> 1
            Route.BookMarkScreen.route -> 2
            else -> 0
        }

    }

    val isBottomBarVisible = remember(key1 = backStackState) {
        backStackState?.destination?.route == Route.HomeScreen.route ||
                backStackState?.destination?.route == Route.SearchScreen.route ||
                backStackState?.destination?.route == Route.BookMarkScreen.route
    }

    Scaffold(modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (isBottomBarVisible) {


                NewsBottomNavigation(
                    items = bottomNavigationItem,
                    selected = selectedItem
                ) { index ->
                    when (index) {
                        0 -> navigateToTab(
                            navController = navController,
                            route = Route.HomeScreen.route
                        )

                        1 -> navigateToTab(
                            navController = navController,
                            route = Route.SearchScreen.route
                        )

                        2 -> navigateToTab(
                            navController = navController,
                            route = Route.BookMarkScreen.route
                        )

                    }

                }
            }
        }) {

        val bottomPadding = it.calculateBottomPadding()
        NavHost(
            navController = navController,
            startDestination = Route.HomeScreen.route,
            modifier = Modifier.padding(bottom = bottomPadding)
        ) {

            composable(route = Route.HomeScreen.route) {
                val viewModel: HomeViewModel = hiltViewModel()

                val articles = viewModel.news.collectAsLazyPagingItems()

                HomeScreen(articles = articles,
                    navigateToSearch = {
                        navigateToTab(navController, route = Route.SearchScreen.route)
                    },
                    navigateToDetails = { article ->
                        navigateToDetails(navController = navController, article = article)
                    }
                )
            }


            composable(route = Route.SearchScreen.route) {
                val viewModel: SearchViewModel = hiltViewModel()
                SearchScreen(state = viewModel.state.value, event = viewModel::onEvent) { article ->
                    navigateToDetails(navController = navController, article = article)
                }
            }

            composable(route = Route.DetailsScreen.route) {


                val viewModel: DetailsViewModel = hiltViewModel()

                if (viewModel.sideEffect != null){
                    Toast.makeText(LocalContext.current , viewModel.sideEffect , Toast.LENGTH_SHORT).show()
                    viewModel.onEvent(DetailsEvent.RemoveSideEffect)

                }

                navController.previousBackStackEntry?.savedStateHandle?.get<Article?>("article")
                    ?.let { article ->
                        DetailsScreen(article = article, event = viewModel::onEvent) {
                            navController.navigateUp()
                        }

                    }
            }


            composable(route = Route.BookMarkScreen.route) {
                val viewModel: BookMarkViewModel = hiltViewModel()
                val state = viewModel.state.value
                BookMarkScreen(state = state) { article ->
                    navigateToDetails(navController = navController, article = article)

                }
            }

        }
    }

}


private fun navigateToTab(navController: NavController, route: String) {

    navController.navigate(route) {
        navController.graph.startDestinationRoute?.let { homeScreen ->
            popUpTo(homeScreen) {
                saveState = true
            }

            restoreState = true

            launchSingleTop = true
        }
    }


}


private fun navigateToDetails(navController: NavController, article: Article) {

    navController.currentBackStackEntry?.savedStateHandle?.set("article", article)

    navController.navigate(Route.DetailsScreen.route)

}