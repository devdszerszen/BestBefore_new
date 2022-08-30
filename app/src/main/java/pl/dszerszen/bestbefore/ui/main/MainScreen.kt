package pl.dszerszen.bestbefore.ui.main

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
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
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import pl.dszerszen.bestbefore.R
import pl.dszerszen.bestbefore.domain.product.model.Product
import pl.dszerszen.bestbefore.ui.common.FullScreenLoader
import pl.dszerszen.bestbefore.ui.main.MainScreenUiIntent.*
import pl.dszerszen.bestbefore.ui.theme.BestBeforeTheme
import pl.dszerszen.bestbefore.ui.theme.dimens
import pl.dszerszen.bestbefore.util.StringValue
import pl.dszerszen.bestbefore.util.asStringValue
import java.time.LocalDate

@Composable
fun MainScreen(viewModel: MainViewModel = hiltViewModel()) {
    val state by viewModel.viewState.collectAsState()
    when {
        state.loaderEnabled -> {
            FullScreenLoader()
        }
        state.showEmptyState -> {
            ProductsEmptyState(state.emptyStateMessage)
        }
        else -> {
            ProductsList(
                modifier = Modifier,
                products = state.products,
                searchText = state.searchText,
                onIntent = { viewModel.onUiIntent((it)) }
            )
        }
    }
}

@Composable
private fun ProductsList(
    products: List<Product>,
    searchText: String,
    modifier: Modifier = Modifier,
    onIntent: (MainScreenUiIntent) -> Unit,
) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val systemUiController = rememberSystemUiController()
    val searchBarColor by animateColorAsState(
        if (products.isEmpty()) colors.error else colors.secondary
    )
    systemUiController.setStatusBarColor(searchBarColor)

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(onClick = { onIntent(OnAddProductClicked) }) {
                Icon(Icons.Default.Add, "add")
            }
        }) { padding ->
        val snackbarMessage = stringResource(R.string.item_removed)
        val snackbarActionLabel = stringResource(R.string.restore)
        Column(Modifier.padding(padding)) {
            Surface(color = searchBarColor) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = dimens.medium),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        shape = RoundedCornerShape(size = dimens.medium),
                        modifier = Modifier.weight(1f),
                        value = searchText,
                        onValueChange = { onIntent(OnSearchTextChanged(it)) },
                        placeholder = { Text("Search") },
                        leadingIcon = { Icon(Icons.Default.Search, null) },
                        trailingIcon = {
                            IconButton(onClick = { onIntent(OnSearchTextChanged("")) }) {
                                Icon(Icons.Default.Close, "clear")
                            }
                        },
                    )
                    IconButton(onClick = { onIntent(OnSortClicked) }) {
                        Icon(Icons.Default.MoreVert, "sort")
                    }
                }
            }
            LazyColumn(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(dimens.small),
                contentPadding = PaddingValues(all = dimens.medium)
            ) {
                items(products, key = { it.id }) { product ->
                    DismissableProductListItem(
                        modifier = Modifier,
                        product = product,
                        onDismiss = { removedProduct ->
                            onIntent(OnProductSwiped(removedProduct))
                            coroutineScope.launch {
                                val result = scaffoldState.snackbarHostState.showSnackbar(
                                    message = snackbarMessage,
                                    actionLabel = snackbarActionLabel
                                )
                                if (result == SnackbarResult.ActionPerformed) {
                                    onIntent(OnRestoreRequested(removedProduct))
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ProductsEmptyState(
    message: StringValue,
) {
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
            Text(text = message.get())
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
            searchText = "",
            onIntent = {}
        )
    }
}

@Preview(uiMode = UI_MODE_NIGHT_NO, name = "Light theme")
@Preview(uiMode = UI_MODE_NIGHT_YES, name = "Dark theme")
@Composable
fun ProductsEmptyStatePreview() {
    BestBeforeTheme(previewMode = true) {
        ProductsEmptyState("Empty state message".asStringValue())
    }
}