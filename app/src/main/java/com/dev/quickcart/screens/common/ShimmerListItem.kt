package com.dev.quickcart.screens.common

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

@Composable
fun ShimmerListItem(
    isLoading: Boolean,
    contentAfterLoading: @Composable () -> Unit = {},
    modifier: Modifier = Modifier
) {

    if (isLoading) {
        LazyRow(
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .height(230.dp)
                        .width(160.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .shimmerEffect()
                ) {}
                Spacer(Modifier.size(17.dp))
                Box(
                    modifier = Modifier
                        .height(230.dp)
                        .width(160.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .shimmerEffect()
                ) {}
                Spacer(Modifier.size(17.dp))
                Box(
                    modifier = Modifier
                        .height(230.dp)
                        .width(160.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .shimmerEffect()
                ) {}
            }
        }

    }
    else {
        contentAfterLoading()
    }


}


fun Modifier.shimmerEffect(): Modifier = composed {

    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition()
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1500)
        )
    )


    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0x80B8B5B5),
                Color(0x808F8B8B),
                Color(0x80B8B5B5),
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    ).onGloballyPositioned(
        onGloballyPositioned = {
            size = it.size
        }
    )

}