package pl.dszerszen.bestbefore.ui.settings

import androidx.compose.foundation.background
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pl.dszerszen.bestbefore.ui.theme.dimens

@Composable
fun ColumnWithDividers(
    modifier: Modifier = Modifier,
    spacing: Dp = dimens.medium,
    divider: @Composable () -> Unit = { Divider() },
    content: @Composable () -> Unit
) {
    SubcomposeLayout(modifier) { constraints ->
        val mainMeasurables = subcompose(ColumnWithDividerSlots.MAIN, content)

        val itemsCount = mainMeasurables.size
        val spacingPx = spacing.roundToPx()

        val dividerMeasurables = subcompose(ColumnWithDividerSlots.DIVIDER) {
            repeat(itemsCount - 1) {
                divider()
            }
        }

        val mainPlaceables = mainMeasurables.map { it.measure(constraints) }
        val dividerPlaceables = dividerMeasurables.map { it.measure(constraints) }

        val totalHeight = mainPlaceables.sumOf { it.height } +
                dividerPlaceables.sumOf { it.height } +
                spacingPx * (itemsCount - 1)

        layout(constraints.maxWidth, totalHeight) {
            var yPosition = 0
            mainPlaceables.forEachIndexed { index, placeable ->
                if (index > 0) {
                    yPosition += spacingPx / 2
                }
                placeable.placeRelative(0, yPosition)
                yPosition += placeable.height
                if (index < itemsCount - 1) {
                    yPosition += spacingPx / 2
                }
                dividerPlaceables.getOrNull(index)?.let { divider ->
                    divider.placeRelative(0, yPosition)
                    yPosition += divider.height
                }
            }
        }
    }
}

enum class ColumnWithDividerSlots {
    MAIN, DIVIDER
}

@Preview
@Composable
fun ColumnWithDividerPreview() {
    Surface {
        ColumnWithDividers(
            spacing = 10.dp,
            content = {
                repeat(5) { Text("Text ${it+1}", modifier = Modifier.background(Color.Red)) }
            }
        )
    }
}