package pl.dszerszen.bestbefore.ui.inapp

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.twotone.Info
import androidx.compose.material.icons.twotone.ThumbUp
import androidx.compose.material.icons.twotone.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import pl.dszerszen.bestbefore.ui.theme.BestBeforeTheme
import pl.dszerszen.bestbefore.ui.theme.dimens

@Composable
fun BottomSheetMessage(inAppMessage: InAppMessage.BottomSheet, onClose: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = dimens.iconSize/2)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            IconButton(onClick = onClose) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "close")
            }
        }
        Icon(
            tint = when (inAppMessage.status) {
                MessageStatus.SUCCESS -> Color.Green
                MessageStatus.INFO -> Color.Gray
                MessageStatus.ERROR -> Color.Red
            },
            modifier = Modifier.size(dimens.iconSize),
            imageVector = when (inAppMessage.status) {
                MessageStatus.SUCCESS -> Icons.TwoTone.ThumbUp
                MessageStatus.INFO -> Icons.TwoTone.Info
                MessageStatus.ERROR -> Icons.TwoTone.Warning
            },
            contentDescription = "message icon"
        )
        Text(text = inAppMessage.title, style = MaterialTheme.typography.h4)
        Spacer(modifier = Modifier.height(dimens.large))
        if (inAppMessage.desc != null) {
            Text(text = inAppMessage.desc, style = MaterialTheme.typography.subtitle2)
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Day")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Night")
@Composable
fun BottomSheetMessageSuccessPreview() {
    BestBeforeTheme(previewMode = true) {
        val successMessage = InAppMessage.BottomSheet("success", "this is success", MessageStatus.SUCCESS)
        BottomSheetMessage(successMessage) {}
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Day")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Night")
@Composable
fun BottomSheetMessageInfoPreview() {
    BestBeforeTheme(previewMode = true) {
        val infoMessage = InAppMessage.BottomSheet("info", "this is info", MessageStatus.INFO)
        BottomSheetMessage(infoMessage) {}
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Day")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Night")
@Composable
fun BottomSheetMessageErrorPreview() {
    BestBeforeTheme(previewMode = true) {
        val errorMessage = InAppMessage.BottomSheet("error", "this is error", MessageStatus.ERROR)
        BottomSheetMessage(errorMessage) {}
    }
}