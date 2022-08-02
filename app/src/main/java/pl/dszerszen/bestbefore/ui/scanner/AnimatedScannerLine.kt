package pl.dszerszen.bestbefore.ui.scanner

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedScannerLine(modifier: Modifier) {
    BoxWithConstraints(modifier = modifier) {
        val transition = rememberInfiniteTransition()
        val offset by transition.animateFloat(
            this.maxHeight.value / -2,
            this.maxHeight.value / 2,
            infiniteRepeatable(tween(3000, easing = LinearEasing), RepeatMode.Reverse)
        )
        val color = Color.White

        Divider(
            modifier = Modifier.align(Alignment.Center)
                .offset(y = offset.dp),
            color = color,
            thickness = 1.dp
        )
    }
}