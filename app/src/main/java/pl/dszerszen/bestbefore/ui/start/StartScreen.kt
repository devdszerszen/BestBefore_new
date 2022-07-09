package pl.dszerszen.bestbefore.ui.start

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.dszerszen.bestbefore.domain.product.model.Product
import pl.dszerszen.bestbefore.ui.common.FullScreenLoader
import pl.dszerszen.bestbefore.ui.theme.BestBeforeTheme

@Composable
fun StartScreen(viewModel: StartViewModel) {
    val state by viewModel.viewState.collectAsState()
    if (state.loaderEnabled) {
        FullScreenLoader()
    } else {
        ProductsList(state.products)
    }
}

@Composable
private fun ProductsList(products: List<Product>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(products) { item ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(Color.Cyan)
                    .padding(4.dp)
            ) {
                Text(item.name)
                Spacer(Modifier.weight(1f))
                Text(item.quantity.toString())
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductListPreview() {
    BestBeforeTheme {
        ProductsList(List(10) { Product("Name $it", quantity = it + 1) })
    }
}