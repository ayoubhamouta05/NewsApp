package com.example.newsapp.presentation.onboarding

import androidx.annotation.DrawableRes
import com.example.newsapp.R

data class Page(
    val title: String,
    val description: String,
    @DrawableRes val img: Int
)


val pages = listOf(

    Page(
        title = "Welcome to Youppix News!",
        description = "Discover the latest news and updates tailored just for you.Get started on your personalized news journey today",
        img = R.drawable.onboarding1
    ),

    Page(
        title = "Tailored Just for You",
        description = "Our smart algorithms analyze your preferences to deliver news articles that match your interests.",
        img = R.drawable.onboarding2
    ),

    Page(
        title = "Stay Up-to-Date",
        description = "From breaking news to in-depth analysis, [Your App Name] keeps you informed with timely updates.",
        img = R.drawable.onboarding3
    ),

)