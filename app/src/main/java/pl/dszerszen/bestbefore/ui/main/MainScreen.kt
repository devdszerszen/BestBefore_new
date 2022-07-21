package pl.dszerszen.bestbefore.ui.main

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pl.dszerszen.bestbefore.domain.product.model.Product
import pl.dszerszen.bestbefore.ui.common.FullScreenLoader
import pl.dszerszen.bestbefore.ui.theme.BestBeforeTheme
import pl.dszerszen.bestbefore.ui.theme.dimens

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val state by viewModel.viewState.collectAsState()
    viewModel.initialize()
    if (state.loaderEnabled) {
        FullScreenLoader()
    } else {
        ProductsList(
            modifier = Modifier.padding(horizontal = dimens.medium),
            products = state.products,
            onButtonClicked = viewModel::onButtonClick,
            onFloatingButtonClicked = viewModel::onFloatingButtonClick,
        )
    }
}

@Composable
private fun ProductsList(
    products: List<Product>,
    modifier: Modifier = Modifier,
    onButtonClicked: () -> Unit,
    onFloatingButtonClicked: () -> Unit
) {
    Scaffold(modifier = modifier, floatingActionButton = {
        FloatingActionButton(onClick = onFloatingButtonClicked) {
            Icon(Icons.Rounded.AddCircle, "add")
        }
    }) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            verticalArrangement = Arrangement.spacedBy(dimens.small),
            contentPadding = PaddingValues(vertical = dimens.medium)
        ) {
            item {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onButtonClicked
                ) {
                    Text("Sample navigation button")
                }
            }
            items(products) { item ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(colors.primary)
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

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ProductListPreview() {
    BestBeforeTheme {
        ProductsList(
            products = List(10) { Product("Name $it", quantity = it + 1) },
            onButtonClicked = {},
            onFloatingButtonClicked = {}
        )
    }
}