package pl.dszerszen.bestbefore.ui.main

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.twotone.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import pl.dszerszen.bestbefore.R
import pl.dszerszen.bestbefore.domain.product.model.Product
import pl.dszerszen.bestbefore.ui.common.FullScreenLoader
import pl.dszerszen.bestbefore.ui.main.MainScreenUiIntent.OnAddProductClicked
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

@Composable
private fun ProductsList(
    products: List<Product>,
    modifier: Modifier = Modifier,
    onIntent: (MainScreenUiIntent) -> Unit,
) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = modifier,
        floatingActionButton = {
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
                            .size(dimens.iconSize),
                        imageVector = Icons.TwoTone.ShoppingCart,
                        contentDescription = ""
                    )
                    Text("It's time to add first product!")
                }
            }
        } else {
            val snackbarMessage = stringResource(R.string.item_removed)
            val snackbarActionLabel = stringResource(R.string.restore)

            LazyColumn(
                modifier = Modifier.padding(padding),
                verticalArrangement = Arrangement.spacedBy(dimens.small),
                contentPadding = PaddingValues(vertical = dimens.medium)
            ) {
                items(products, key = { it.id }) { product ->
                    DismissableProductListItem(
                        product = product,
                        onDismiss = { removedProduct ->
                            onIntent(MainScreenUiIntent.OnProductSwiped(removedProduct))
                            coroutineScope.launch {
                                val result = scaffoldState.snackbarHostState.showSnackbar(
                                    message = snackbarMessage,
                                    actionLabel = snackbarActionLabel
                                )
                                if (result == SnackbarResult.ActionPerformed) {
                                    onIntent(MainScreenUiIntent.OnRestoreRequested(removedProduct))
                                }
                            }
                        }
                    )
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

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun EmptyListPreview() {
    BestBeforeTheme {
        ProductsList(
            products = emptyList(),
            onIntent = {}
        )
    }
}