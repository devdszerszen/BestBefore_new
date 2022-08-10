package pl.dszerszen.bestbefore.ui.main

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import pl.dszerszen.bestbefore.domain.product.model.Product
import pl.dszerszen.bestbefore.ui.theme.BestBeforeTheme
import pl.dszerszen.bestbefore.ui.theme.dimens
import java.time.LocalDate

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DismissableProductListItem(
    product: Product,
    onDismiss: (Product) -> Unit,
    modifier: Modifier = Modifier,

    ) {
    val dismissState = rememberDismissState()
    if (dismissState.isDismissed(DismissDirection.EndToStart)) {
        LaunchedEffect(dismissState) {
            onDismiss(product)
            dismissState.snapTo(DismissValue.Default)
        }
    }

    SwipeToDismiss(
        modifier = modifier,
        state = dismissState,
        directions = setOf(DismissDirection.EndToStart),
        background = {
            val color by animateColorAsState(
                when (dismissState.targetValue) {
                    DismissValue.Default -> colors.background
                    else -> Color.Red
                }
            )
            val scale by animateFloatAsState(
                if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
            )

            Box(
                Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = dimens.medium),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    modifier = Modifier.scale(scale),
                    tint = colors.onBackground
                )
            }
        },
        dismissThresholds = { FractionalThreshold(0.2f) }
    ) {
        Surface(color = colors.primary) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(dimens.medium)
            ) {
                Text(product.name)
                Spacer(Modifier.weight(1f))
                Text(product.quantity.toString())
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ProductListItemPreview() {
    BestBeforeTheme {
        DismissableProductListItem(
            product = Product(
                name = "Name",
                desc = "desc",
                quantity = 2,
                date = LocalDate.parse("2021-01-21"),
                id = "123"
            ),
            onDismiss = {}
        )
    }
}