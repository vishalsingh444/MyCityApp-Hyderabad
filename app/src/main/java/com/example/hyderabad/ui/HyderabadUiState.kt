package com.example.hyderabad.ui

import com.example.hyderabad.R
import com.example.hyderabad.data.CategoryType
import com.example.hyderabad.data.Recommendation
import com.example.hyderabad.data.local.LocalRecommendationDataProvider.listOfRecommendation

data class HyderabadUiState (
    val categoryType: Map<CategoryType,List<Recommendation>> = emptyMap(),
    val currentCategory: CategoryType = CategoryType.Cafes,
    val currentSelectedRecommendation: Recommendation = listOfRecommendation[0],
    val isShowingHomePage: Boolean = true,
    val isShowingRecommendationList: Boolean = false,
    val isShowingRecommendation: Boolean = false,
    val currentCategoryName: Int = R.string.cafes
)