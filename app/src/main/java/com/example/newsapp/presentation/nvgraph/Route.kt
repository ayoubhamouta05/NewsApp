package com.example.newsapp.presentation.nvgraph

sealed class Route(
        val route : String
) {

    object OnBoardingScreen : Route(route = "onBoardingScreen")

    object HomeScreen : Route(route = "homeScreen")

    object SearchScreen : Route(route = "searchScreen")

    object BookMarkScreen : Route(route = "bookMarkScreen")
    object DetailsScreen : Route(route = "detailsScreen")

    object AppStartNavigation : Route(route = "appStartNavigation")

    object NewsNavigation : Route(route = "newsNavigation")

    object NewsNavigationScreen : Route(route = "newsNavigationScreen")


}