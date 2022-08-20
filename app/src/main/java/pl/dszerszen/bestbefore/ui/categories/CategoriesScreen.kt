package pl.dszerszen.bestbefore.ui.categories

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import pl.dszerszen.bestbefore.R
import pl.dszerszen.bestbefore.domain.product.model.Category
import pl.dszerszen.bestbefore.domain.product.model.CategoryIcon
import pl.dszerszen.bestbefore.ui.categories.CategoriesScreenUiIntent.*
import pl.dszerszen.bestbefore.ui.theme.BestBeforeTheme
import pl.dszerszen.bestbefore.ui.theme.dimens
import java.util.*
import kotlin.random.Random

/**
 * TODO
 * Whole screen must be improved.
 * Current implementation is ugly but provides basic functionality!
 */

@Composable
fun CategoriesScreen(viewModel: CategoriesViewModel = hiltViewModel()) {
    val state by viewModel.viewState.collectAsState()
    Box(Modifier.fillMaxSize()) {
        if (state.isInEditMode) {
            CategoryEditor(
                modifier = Modifier.align(Alignment.Center),
                name = state.editedCategoryName,
                icon = state.editedCategoryIcon,
                onUiIntent = viewModel::onUiIntent
            )
        } else {
            CategoriesScreen(state, viewModel::onUiIntent)
        }
    }
}

@Composable
private fun CategoriesScreen(state: CategoriesViewState, onUiIntent: (CategoriesScreenUiIntent) -> Unit) {
    CategoriesList(
        modifier = Modifier.fillMaxSize(),
        categories = state.categories,
        onCategoryAdded = { onUiIntent(OnAddClicked) },
        onCategoryDeleted = { onUiIntent(OnCategoryDeleted(it)) },
        onCategoryEdited = { onUiIntent(OnEditClicked(it)) }
    )
}

@Composable
fun CategoryEditor(
    modifier: Modifier,
    name: String,
    icon: CategoryIcon?,
    onUiIntent: (CategoriesScreenUiIntent) -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { onUiIntent(OnNameEdited(it)) },
            label = { Text("Category name") }
        )
        Button(onClick = { onUiIntent(OnCategorySaved) }) {
            Text(stringResource(R.string.save))
        }
    }
}

@Composable
private fun CategoriesList(
    modifier: Modifier,
    categories: List<Category>,
    onCategoryAdded: (Category) -> Unit,
    onCategoryDeleted: (Category) -> Unit,
    onCategoryEdited: (Category) -> Unit,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(dimens.medium)) {
        if (categories.isNotEmpty()) {
            Text("Categories:")
            categories.forEach { category ->
                Surface(color = MaterialTheme.colors.secondary) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = dimens.small),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        category.icon?.let { icon ->
                            Icon(icon.icon, "")
                            Spacer(Modifier.width(dimens.medium))
                        }
                        Text(category.name)
                        Spacer(Modifier.weight(1f))
                        IconButton(onClick = { onCategoryEdited(category) }) {
                            Icon(Icons.Default.Edit, "edit")
                        }
                        IconButton(onClick = { onCategoryDeleted(category) }) {
                            Icon(Icons.Default.Delete, "delete")
                        }
                    }
                }
            }
            Spacer(Modifier.height(dimens.medium))
        }
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
                onCategoryAdded(
                    Category(
                        id = UUID.randomUUID().toString(),
                        name = "Category ${Random.nextInt()}"
                    )
                )
            }) {
            Text("Add next category")
        }
    }
}

@Preview
@Composable
fun CateSettingsPreview() {
    val state = CategoriesViewState().copy(
        categories = listOf(
            Category("1", "category icon", CategoryIcon.DEFAULT),
            Category("2", "category NO icon"),
        )
    )
    BestBeforeTheme {
        CategoriesScreen(state) {}
    }
}