package pl.dszerszen.bestbefore.ui.categories

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import pl.dszerszen.bestbefore.domain.product.model.Category
import pl.dszerszen.bestbefore.ui.theme.BestBeforeTheme
import pl.dszerszen.bestbefore.ui.theme.Typography
import pl.dszerszen.bestbefore.ui.theme.dimens
import pl.dszerszen.bestbefore.ui.theme.smallLight

@Composable
fun CategoriesRow(
    categories: List<Category>,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = Typography.smallLight,
    textPadding: PaddingValues = PaddingValues(all = dimens.medium),
    onClick: (Category, checked: Boolean) -> Unit
) {
    FlowRow(
        modifier = modifier,
        mainAxisSpacing = dimens.small,
        mainAxisAlignment = FlowMainAxisAlignment.Center
    ) {
        categories.forEach { category ->
            CategoryItemInRow(
                category = category,
                textStyle = textStyle,
                textPadding = textPadding,
            ) { onClick(category, category.selected.not()) }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategoryItemInRow(
    category: Category,
    color: Color = colors.secondary,
    textStyle: TextStyle,
    textPadding: PaddingValues,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(if (category.selected) color else Color.Transparent)

    Surface(
        shape = RoundedCornerShape(dimens.medium),
        border = BorderStroke(1.dp, color),
        color = backgroundColor,
        onClick = onClick
    ) {
        Text(
            modifier = Modifier.padding(textPadding),
            text = category.name,
            style = textStyle
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light theme")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark theme")
@Composable
fun CategoriesRowPreview() {
    BestBeforeTheme(previewMode = true) {
        val categories = List(10) {
            Category(
                id = it.toString(),
                name = "Category ${it + 1}",
                selected = it == 2
            )
        }
        CategoriesRow(
            categories = categories,
            onClick = { _, _ -> }
        )
    }
}



