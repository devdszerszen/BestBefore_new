package pl.dszerszen.bestbefore.ui.common

import android.content.res.Configuration
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.dszerszen.bestbefore.ui.theme.BestBeforeTheme
import pl.dszerszen.bestbefore.ui.theme.Typography
import pl.dszerszen.bestbefore.ui.theme.bigNormal
import pl.dszerszen.bestbefore.ui.theme.dimens

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun QuantitySelector(
    quantity: Int,
    modifier: Modifier = Modifier,
    buttonColor: Color = colors.primary,
    textColor: Color = colors.primary,
    step: Int = 1,
    minQuantity: Int = 1,
    onQuantityChanged: (Int) -> Unit
) {

    var cachedQuantity by remember { mutableStateOf(quantity) }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = {
            if (quantity - step >= minQuantity) {
                onQuantityChanged(quantity - step)
            }
        }) {
            Icon(
                modifier = Modifier
                    .border(1.dp, color = buttonColor, CircleShape)
                    .padding(dimens.small),
                imageVector = Icons.Filled.KeyboardArrowDown,
                tint = buttonColor,
                contentDescription = "minus"
            )
        }
        Box(Modifier.width(48.dp)) {
            AnimatedContent(
                modifier = Modifier.align(Alignment.Center),
                targetState = quantity,
                transitionSpec = {
                    addAnimation(quantity > cachedQuantity).using(
                        SizeTransform(clip = false)
                    )
                }
            ) { targetState ->
                Text(
                    text = targetState.toString(),
                    style = Typography.bigNormal,
                    color = textColor,
                )
                cachedQuantity = targetState
            }
        }
        IconButton(onClick = { onQuantityChanged(quantity + step) }) {
            Icon(
                modifier = Modifier
                    .border(1.dp, color = buttonColor, CircleShape)
                    .padding(dimens.small),
                imageVector = Icons.Filled.KeyboardArrowUp,
                tint = buttonColor,
                contentDescription = "plus"
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun addAnimation(
    increase: Boolean = true,
    durationMillis: Int = 300
): ContentTransform {
    return slideInVertically(
        animationSpec = tween(durationMillis = durationMillis),
        initialOffsetY = { fullHeight -> if (increase) -fullHeight else fullHeight }
    ) + fadeIn(
        animationSpec = tween(durationMillis = durationMillis)
    ) with slideOutVertically(
        animationSpec = tween(durationMillis = durationMillis),
        targetOffsetY = { fullHeight -> if (increase) fullHeight else -fullHeight }
    ) + fadeOut(
        animationSpec = tween(durationMillis = durationMillis)
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light theme")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark theme")
@Composable
fun QuantitySelectorPreview() {
    BestBeforeTheme(previewMode = true) {
        QuantitySelector(1) {}
    }
}

