package com.example.hyderabad.data.local

import com.example.hyderabad.R
import com.example.hyderabad.data.Category
import com.example.hyderabad.data.CategoryType

object LocalCategoryDataProvider {
    val listOfCategory = listOf(
        Category(
            name = "Cafes",
            image = R.drawable.cafe,
            categoryType = CategoryType.Cafes
        ),
        Category(
            name = "Restaurants",
            image = R.drawable.restaurants,
            categoryType = CategoryType.Restaurants
        ),
        Category(
            name = "Tourist Places",
            image = R.drawable.touristplaces,
            categoryType = CategoryType.TouristPlaces
        )
    )
}