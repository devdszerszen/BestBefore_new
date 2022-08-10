package pl.dszerszen.bestbefore.ui.main

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.twotone.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import pl.dszerszen.bestbefore.domain.product.model.Product
import pl.dszerszen.bestbefore.ui.common.FullScreenLoader
import pl.dszerszen.bestbefore.ui.main.MainScreenUiIntent.OnAddProductClicked
import pl.dszerszen.bestbefore.ui.main.MainScreenUiIntent.OnProductSwiped
import pl.dszerszen.bestbefore.ui.theme.BestBeforeTheme
import pl.dszerszen.bestbefore.ui.theme.dimens
import java.time.LocalDate

@Composable
fun MainScreen(viewModel: MainViewModel = hiltViewModel()) {
    val state by viewModel.viewState.collectAsState()
    if (state.loaderEnabled) {
        FullScreenLoader()
    } else {
        ProductsList(
            modifier = Modifier.padding(horizontal = dimens.medium),
            products = state.products,
            onIntent = { viewModel.onUiIntent((it)) }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ProductsList(
    products: List<Product>,
    modifier: Modifier = Modifier,
    onIntent: (MainScreenUiIntent) -> Unit,
) {
    Scaffold(modifier = modifier, floatingActionButton = {
        FloatingActionButton(onClick = { onIntent(OnAddProductClicked) }) {
            Icon(Icons.Default.Add, "add")
        }
    }) { padding ->
        if (products.isEmpty()) {
            Box(Modifier.fillMaxSize()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Icon(
                        modifier = Modifier
                            .size(128.dp),
                        imageVector = Icons.TwoTone.ShoppingCart,
                        contentDescription = ""
                    )
                    Text("It's time to add first product!")
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                verticalArrangement = Arrangement.spacedBy(dimens.small),
                contentPadding = PaddingValues(vertical = dimens.medium)
            ) {
                items(products, key = { it.id }) { item ->
                    val dismissState = rememberDismissState()
                    if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                        onIntent(OnProductSwiped(item))
                    }
                    SwipeToDismiss(
                        state = dismissState,
                        directions = setOf(DismissDirection.EndToStart),
                        background = {
                            val color by animateColorAsState(
                                when (dismissState.targetValue) {
                                    DismissValue.Default -> Color.White
                                    else -> Color.Red
                                }
                            )
                            val alignment = Alignment.CenterEnd
                            val icon = Icons.Default.Delete

                            val scale by animateFloatAsState(
                                if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
                            )

                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .background(color)
                                    .padding(horizontal = dimens.medium),
                                contentAlignment = alignment
                            ) {
                                Icon(
                                    icon,
                                    contentDescription = "Delete Icon",
                                    modifier = Modifier.scale(scale)
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
                                Text(item.name)
                                Spacer(Modifier.weight(1f))
                                Text(item.quantity.toString())
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ProductListPreview() {
    BestBeforeTheme {
        ProductsList(
            products = List(10) { Product("Name $it", quantity = it + 1, date = LocalDate.parse("2020-01-01")) },
            onIntent = {}
        )
    }
}