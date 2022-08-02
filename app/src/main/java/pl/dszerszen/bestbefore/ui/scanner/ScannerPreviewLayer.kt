package pl.dszerszen.bestbefore.ui.scanner

import androidx.annotation.FloatRange
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.tooling.preview.Preview
import pl.dszerszen.bestbefore.ui.scanner.VerticalRatio.Custom
import pl.dszerszen.bestbefore.ui.scanner.VerticalRatio.Square
import pl.dszerszen.bestbefore.ui.theme.BestBeforeTheme

sealed class VerticalRatio {
    object Square : VerticalRatio()
    class Custom(@FloatRange(from = 0.0, to = 1.0) val ratio: Float) : VerticalRatio()
}

@Composable
fun ScannerPreviewLayer(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Black.copy(alpha = 0.5f),
    @FloatRange(from = 0.0, to = 1.0) horizontalRatio: Float = 0.8f,
    verticalRatio: VerticalRatio = Square,
    @FloatRange(from = 0.0, to = 1.0) angleLengthRatio: Float = 0.4f,
    angleOffset: Float = 20f,
    contentBelow: @Composable BoxScope.() -> Unit = {},
    contentBehind: @Composable () -> Unit,
) {
    BoxWithConstraints(modifier) {
        contentBehind()
        Canvas(Modifier.fillMaxSize()) {
            val left = (size.width - (size.width * horizontalRatio)) / 2
            val right = size.width - left
            val top = when (verticalRatio) {
                is Custom -> (size.height - (size.height * verticalRatio.ratio)) / 2
                Square -> (size.height / 2) - (right - left) / 2
            }
            val bottom = size.height - top

            val angleLength = (right - left) / 2 * angleLengthRatio

            val angles = Path().apply {
                moveTo(left - angleOffset, top - angleOffset + angleLength)
                lineTo(left - angleOffset, top - angleOffset)
                lineTo(left - angleOffset + angleLength, top - angleOffset)
                moveTo(right - angleLength + angleOffset, top - angleOffset)
                lineTo(right + angleOffset, top - angleOffset)
                lineTo(right + angleOffset, top - angleOffset + angleLength)
                moveTo(right + angleOffset, bottom - angleLength + angleOffset)
                lineTo(right + angleOffset, bottom + angleOffset)
                lineTo(right - angleLength + angleOffset, bottom + angleOffset)
                moveTo(left - angleOffset + angleLength, bottom + angleOffset)
                lineTo(left - angleOffset, bottom + angleOffset)
                lineTo(left - angleOffset, bottom + angleOffset - angleLength)
            }

            clipRect(
                left = left,
                right = right,
                top = top,
                bottom = bottom,
                clipOp = ClipOp.Difference
            ) {
                drawRect(size = Size(size.width, size.height), color = backgroundColor)
            }
            drawPath(path = angles, color = Color.White, style = Stroke(width = 4f, cap = StrokeCap.Round))
        }
        AnimatedScannerLine(
            Modifier
                .align(Alignment.Center)
                .size(
                    width = maxWidth * horizontalRatio,
                    height = when (verticalRatio) {
                        is Custom -> maxHeight.times(verticalRatio.ratio)
                        Square -> maxWidth * horizontalRatio
                    }
                )
        )
        Box(
            Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(maxHeight / 5) //TODO calculate proper value
        ) {
            contentBelow()
        }
    }
}

@Preview
@Composable
fun ScannerPreviewLayerPreview() {
    BestBeforeTheme() {
        ScannerPreviewLayer(
            modifier = Modifier.fillMaxSize(),
            contentBelow = {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Red)
                ) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.Center),
                        text = "Sample text in box"
                    )
                }
            }
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Blue)
            )
        }
    }
}