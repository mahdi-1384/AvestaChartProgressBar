package ir.avesta.avestachartprogressbar

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

val DefaultDelay = 100L
val DefaultDuration = 600

@Composable
fun ChartProgressBar(
    minHeight: Dp,
    maxHeight: Dp,
    count: Int = 4,
    verticalAlignment: Alignment.Vertical = Alignment.Bottom,
    animation: DurationBasedAnimationSpec<Dp> = tween(durationMillis = DefaultDuration),
    itemDelay: ((Int) -> Long)? = null,
    chartItem: @Composable ((Dp) -> Unit)? = null,
) {
    Row(
        modifier = Modifier.height(maxHeight),
        verticalAlignment = verticalAlignment
    ) {

        (0 until count).forEach {index ->

            val infiniteTransition = rememberInfiniteTransition()
            var targetHeight by remember { mutableStateOf(minHeight) }

            val height by infiniteTransition.animateValue(
                initialValue = minHeight,
                targetValue = targetHeight,
                typeConverter = TwoWayConverter({ AnimationVector1D(it.value) }, { it.value.dp }),
                animationSpec = infiniteRepeatable(
                    animation = animation,
                    repeatMode = RepeatMode.Reverse
                )
            )

            LaunchedEffect(Unit) {

                val itemDelay = if (itemDelay == null) index * DefaultDelay else itemDelay(index)

                if (targetHeight == minHeight) {
                    delay(itemDelay)
                    targetHeight = maxHeight
                }
            }

            if (chartItem == null)
                DefaultChartItem(height = height)
            else
                chartItem(height)
        }
    }
}

@Composable
private fun DefaultChartItem(height: Dp) {
    Box(
        modifier = Modifier
            .padding(horizontal = 2.dp)
            .width(5.dp)
            .height(height)
            .background(Color.Blue)
    )
}