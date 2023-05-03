package com.example.hyderabad.ui

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hyderabad.data.Category
import com.example.hyderabad.data.CategoryType
import com.example.hyderabad.data.Recommendation
import com.example.hyderabad.data.local.LocalCategoryDataProvider.listOfCategory
import com.example.hyderabad.data.local.LocalRecommendationDataProvider.listOfRecommendation
import com.example.hyderabad.ui.theme.HyderabadTheme
import com.example.hyderabad.R
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hyderabad.ui.util.HyderabadContentType


@Composable
fun HyderabadApp(
    windowSize : WindowWidthSizeClass
){
    val viewModel: HyderabadViewModel = viewModel()
    val uiState = viewModel.uiState.collectAsState().value
    val contentType : HyderabadContentType

    contentType = when(windowSize){
        WindowWidthSizeClass.Compact -> HyderabadContentType.ListOnly
        WindowWidthSizeClass.Medium -> HyderabadContentType.ListAndDetails
        WindowWidthSizeClass.Expanded -> HyderabadContentType.ListAndDetails
        else -> HyderabadContentType.ListOnly
    }
    HyderabadHomeScreen(
        contentType = contentType,
        viewModel = viewModel,
        categoryType = uiState.currentCategory,
        uiState = uiState
    )
}

@Composable
fun HyderabadAppBar(

    topAppBarName: Int,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    canNavigateBack: Boolean
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        if(canNavigateBack){
            IconButton(
                onClick = onBackPressed,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .background(MaterialTheme.colorScheme.surface, shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "back navigation"
                )
            }
        }
        Text(
            text = stringResource(id = topAppBarName),
            fontSize = 28.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
@Composable
fun HyderabadHomeScreen(
    contentType: HyderabadContentType,
    viewModel: HyderabadViewModel,
    uiState: HyderabadUiState,
    categoryType: CategoryType,
) {
    if(contentType==HyderabadContentType.ListAndDetails){
        val activity = LocalContext.current as Activity
        Row{
            Column (modifier = Modifier.weight(1f)){
                CategoryList(
                    categories = listOfCategory,
                    viewModel = viewModel,
                    isFullScreen = true
                )
            }
            Column (modifier = Modifier.weight(1f)){
                RecommendationList(
                    viewModel = viewModel,
                    uiState = uiState,
                    recommendations = uiState.categoryType[categoryType]?: listOfRecommendation,
                    onBackPressed = { activity.finish()},
                    isFullScreen = true,
                )
            }
            Column (modifier = Modifier.weight(1f)){
                RecommendationDetails(
                    recommendation = uiState.currentSelectedRecommendation,
                    onBackPressed = {activity.finish()},
                    isFullScreen = true
                )
            }
        }
    }
    else{
        if(uiState.isShowingHomePage){
            CategoryList(
                viewModel = viewModel,
                categories = listOfCategory,
                isFullScreen = false
            )
        }
        else if(uiState.isShowingRecommendationList){
            RecommendationList(
                viewModel = viewModel,
                recommendations = uiState.categoryType[categoryType]?: listOfRecommendation,
                onBackPressed = {viewModel.updateCurrentScreen(1)},
                uiState = uiState,
                isFullScreen = false
            )
        }
        else if(uiState.isShowingRecommendation){
            RecommendationDetails(
                recommendation = uiState.currentSelectedRecommendation,
                onBackPressed = {viewModel.updateCurrentScreen(2)},
                isFullScreen = false
            )
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendationCardItem(
    viewModel: HyderabadViewModel,
    recommendation: Recommendation,
    id: Int,
    modifier: Modifier = Modifier,
){
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(8.dp))
            .shadow(elevation = 10.dp, shape = RoundedCornerShape(8.dp))
            .clickable { viewModel.updateSelectedRecommendation(id) },
        elevation = CardDefaults.cardElevation(hoveredElevation = 10.dp),
        onClick = {viewModel.updateSelectedRecommendation(id)}
        ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp)),
        ) {
            Image(
                painter = painterResource(id = recommendation.image),
                contentDescription = stringResource(id = recommendation.title),
                modifier = Modifier
                    .heightIn(max = 120.dp, min = 120.dp)
                    .width(120.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .heightIn(max = 120.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource(id = recommendation.title),
                    fontSize = 26.sp
                )
                Spacer(Modifier.height(8.dp))
                Text(text = stringResource(id = recommendation.subtitle))
            }
        }
    }
}
@Composable
fun CategoryCardItem(
    viewModel: HyderabadViewModel,
    category: Category,
    categoryType: CategoryType,
    modifier:Modifier = Modifier
){
    Card(
        modifier = modifier
            .height(120.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                viewModel.updateCurrentCategory(categoryType)
            },
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = category.image),
                contentDescription = category.name,
                modifier = Modifier
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop,
            )
            Image(
                painter = painterResource(id = R.drawable.a_black_image),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop,
                alpha = 0.5f
            )
            Text(
                text = category.name,
                fontSize = 32.sp,
                color = Color.White
            )
        }
    }
}

@Composable
fun CategoryList(
    isFullScreen: Boolean,
    categories: List<Category>,
    viewModel: HyderabadViewModel,
    modifier: Modifier = Modifier,
){
    val topAppBarName = if(isFullScreen) R.string.app_name else R.string.category_heading
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.padding(start=8.dp,end = 8.dp)
    ) {
        items(1){
           HyderabadAppBar(
               topAppBarName = topAppBarName,
               onBackPressed = {},
               canNavigateBack = false
           )
        }
        items(categories){category->
            CategoryCardItem(
                category = category,
                viewModel = viewModel,
                categoryType = category.categoryType
            )
        }
    }
}
@Composable
fun RecommendationList(
    viewModel: HyderabadViewModel,
    uiState: HyderabadUiState,
    recommendations: List<Recommendation>,
    onBackPressed: () -> Unit,
    isFullScreen: Boolean,
    modifier:Modifier = Modifier
){
    BackHandler {
        onBackPressed()
    }
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.padding(start = 8.dp,end = 8.dp)
    ){

        items(1){
            HyderabadAppBar(
                topAppBarName = uiState.currentCategoryName,
                onBackPressed = onBackPressed,
                canNavigateBack = !isFullScreen
            )
        }
        items(recommendations){recommendation->
            RecommendationCardItem(
                viewModel = viewModel,
                recommendation = recommendation,
                id = recommendation.id
            )
        }
    }
}
@Composable
fun RecommendationDetails(
    recommendation: Recommendation,
    onBackPressed: () -> Unit,
    isFullScreen: Boolean,
    modifier:Modifier = Modifier,
){
    BackHandler {
        onBackPressed()
    }

    Column {
        HyderabadAppBar(topAppBarName = recommendation.title, onBackPressed = onBackPressed, canNavigateBack = !isFullScreen )
        LazyColumn(
            modifier = modifier.padding(start = 8.dp,end = 8.dp)
        ){
            items(1){
                Card (
                    modifier = modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                ){
                    Column {
                        Image(painter = painterResource(id = recommendation.image),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp),
                            contentScale = ContentScale.Crop
                        )

                        Text(
                            text = stringResource(id = recommendation.recommendationDetails),
                            fontSize = 18.sp,
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxSize()

                        )
                    }
                }
            }
        }

    }
    
}
//@Preview(showBackground = true)
//@Composable
//fun CategoryCardItemPreview(){
//    CategoryCardItem(
//        viewModel = viewModel(),
//        category = listOfCategory[2],
//        categoryType = CategoryType.Cafes,
//    )
//}
//
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun CategoryListPreview(){
//    CategoryList(
//        categories = listOfCategory,
//        viewModel = viewModel()
//    )
//}
@Preview(showBackground = true, widthDp = 1000)
@Composable
fun HyderabadAppPreview(){
    HyderabadTheme {
        HyderabadApp(windowSize = WindowWidthSizeClass.Expanded)
    }
}
//
//
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun RecommendationCardItemPreview(){
//    RecommendationCardItem(recommendation = listOfRecommendation[0], onClicked = {})
//}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RecommendationListPreview(){
    RecommendationList(viewModel = viewModel(), recommendations = listOfRecommendation, onBackPressed = {}, uiState = HyderabadUiState(), isFullScreen = false)
}

@Preview(showSystemUi = true)
@Composable
fun RecommendationDetailsPreview(){
    val viewModel: HyderabadViewModel = viewModel()
    val uiState = viewModel.uiState.collectAsState().value
    viewModel.updateSelectedRecommendation(0)
    RecommendationDetails(recommendation = uiState.currentSelectedRecommendation, onBackPressed = {},isFullScreen = false)
}

@Preview( widthDp = 1000)
@Composable
fun HomeScreenPreview(){
    val viewModel: HyderabadViewModel = viewModel()
    val uiState = viewModel.uiState.collectAsState().value
    HyderabadHomeScreen(
        contentType = HyderabadContentType.ListAndDetails,
        viewModel = viewModel,
        uiState = uiState,
        categoryType = CategoryType.Cafes
    )
}