package com.android.vengateshm.composearsenal.presentation.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.android.vengateshm.composearsenal.R
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlin.math.floor

@ExperimentalUnitApi
@ExperimentalPagerApi
@Composable
fun AutoSlider() {
    val topPlaces = getPlaces()
    val pagerState = rememberPagerState(
        pageCount = topPlaces.size,
        initialOffscreenLimit = 2
    )
    LaunchedEffect(key1 = Unit, block = {
        while (true) {
            //yield()
            delay(2000)
            pagerState.animateScrollToPage(
                page = (pagerState.currentPage + 1) % pagerState.pageCount,
                animationSpec = tween(600)
            )
        }
    })

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(text = "Top Restaurants")
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalPager(state = pagerState) { page ->
            val place = topPlaces[page]
            PlaceCard(
                place,
                R.drawable.thumb_image,
                R.drawable.ic_baseline_location_on_24,
                R.drawable.ic_baseline_star_rate_24
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@ExperimentalUnitApi
@Composable
private fun PlaceCard(place: Place, thumbImgRes: Int, locImgRes: Int, ratingImgRes: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        elevation = 8.dp,
        shape = RoundedCornerShape(24.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        listOf(
                            Color(place.linearGradientStartColor.toColorFormat()),
                            Color(place.linearGradientEndColor.toColorFormat())
                        )
                    )
                )
        ) {
            Canvas(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(width = 100.dp, height = 150.dp),
            ) {
                val radius = 24.0f
                drawPath(
                    path = Path().apply {
                        moveTo(0f, size.height)
                        lineTo(size.width - radius, size.height)
                        quadraticBezierTo(
                            size.width, size.height, size.width, size.height - radius
                        )
                        lineTo(size.width, radius)
                        quadraticBezierTo(size.width, 0f, size.width - radius, 0f)
                        lineTo(size.width - 1.5f * radius, 0f)
                        quadraticBezierTo(-radius, 2 * radius, 0f, size.height)
                        close()
                    },
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(place.linearGradientStartColor.toColorFormat()),
                            Color(place.linearGradientEndColor.toColorFormat())
                        )
                    )
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxSize(), verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .size(64.dp, 64.dp),
                    painter = painterResource(id = thumbImgRes),
                    contentDescription = null
                )
                Column(
                    modifier = Modifier
                        .weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = place.name,
                        style = TextStyle(
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif
                        )
                    )
                    Text(
                        text = place.category,
                        style = TextStyle(
                            color = Color.White,
                            fontFamily = FontFamily.SansSerif
                        )
                    )
                    Spacer(Modifier.height(16.dp))
                    Row {
                        Image(
                            painter = painterResource(locImgRes),
                            contentDescription = null
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = place.location,
                            style = TextStyle(
                                color = Color.White,
                                fontFamily = FontFamily.SansSerif,
                                fontSize = TextUnit(12f, TextUnitType.Sp)
                            )
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .padding(end = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = place.rating.toString(),
                        style = TextStyle(
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif,
                            fontSize = TextUnit(12f, TextUnitType.Sp)
                        )
                    )
                    RatingBar(place.rating, ratingImgRes)
                }
            }
        }
    }
}

@Composable
fun RatingBar(rating: Double, iconRes: Int) {
    val flooredRating = floor(rating)
    Row {
        repeat(flooredRating.toInt()) {
            Image(
                modifier = Modifier.size(16.dp, 16.dp),
                painter = painterResource(id = iconRes),
                contentDescription = "Rating star"
            )
        }
    }
}

data class Place(
    val name: String,
    val category: String,
    val location: String,
    val rating: Double,
    val linearGradientStartColor: String,
    val linearGradientEndColor: String,
)

fun getPlaces(): List<Place> = listOf(
    Place(
        "Dubai Mall Food Court",
        "Cosy · Casual · Good for kids",
        "Dubai · In The Dubai Mall",
        4.4,
        "#6DC8F3",
        "#73A1F9"
    ),
    Place(
        "Hamriyah Food Court",
        "All you can eat · Casual · Groups",
        "Sharjah",
        3.7,
        "#FFB157",
        "#FFA057"
    ),
    Place(
        "Gate of Food Court",
        "Casual · Groups",
        "Dubai · Near Dubai Aquarium",
        4.5,
        "#FF5B95",
        "#F8556D"
    ),
    Place(
        "Express Food Court",
        "Cosy · Casual · Good for kids",
        "Dubai · In The Dubai Mall",
        4.1,
        "#D76EF5",
        "#8F7AFE"
    ),
    Place(
        "BurJuman Food Court",
        "Self service · Stand in",
        "Dubai · In BurJuman",
        2.8,
        "#42E695",
        "#3BB2B8"
    )
)

fun String.toColorFormat(): Int = android.graphics.Color.parseColor(this)