package com.example.hyderabad.ui

import androidx.lifecycle.ViewModel
import com.example.hyderabad.R
import com.example.hyderabad.data.CategoryType
import com.example.hyderabad.data.Recommendation
import com.example.hyderabad.data.local.LocalRecommendationDataProvider.listOfRecommendation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class HyderabadViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(HyderabadUiState())
    val uiState: StateFlow<HyderabadUiState> = _uiState

    init{
        initializeUiState()
    }

    private fun initializeUiState(){
        var categoryRecommendation:Map<CategoryType,List<Recommendation>> =
            listOfRecommendation.groupBy { it.categoryType }
        _uiState.value =
            HyderabadUiState(
                categoryType = categoryRecommendation,
                currentSelectedRecommendation = categoryRecommendation[CategoryType.Cafes]?.get(0)
                    ?:listOfRecommendation[0],
                isShowingHomePage = true
            )

    }

    fun updateCurrentCategory(currentCategory:CategoryType){
        val currentCategoryName = when(currentCategory){
            CategoryType.Cafes -> R.string.cafes
            CategoryType.Restaurants -> R.string.restaurants
            CategoryType.TouristPlaces -> R.string.tourist_places
        }
        _uiState.update {
            it.copy(
                currentCategory = currentCategory,
                currentCategoryName = currentCategoryName,
                isShowingHomePage = false,
                isShowingRecommendationList = true,
                isShowingRecommendation = false

            )
        }
    }
    fun updateCurrentScreen(screen: Int){
        _uiState.update {
            it.copy(
                isShowingHomePage = screen==1,
                isShowingRecommendation = screen==3,
                isShowingRecommendationList = screen==2
            )
        }
    }
    fun updateSelectedRecommendation(id: Int){
        _uiState.update {
            it.copy(
                currentSelectedRecommendation = listOfRecommendation[id],
                isShowingRecommendationList = false,
                isShowingRecommendation = true,
                isShowingHomePage = false
            )
        }
    }
}