package pl.dszerszen.bestbefore.ui.common

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pl.dszerszen.bestbefore.ui.theme.BestBeforeTheme

@Composable
fun FullScreenLoader() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO, name = "Day")
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES, name = "Night")
@Composable
fun FullScreenLoaderPreview() {
    BestBeforeTheme{
        FullScreenLoader()
    }
}