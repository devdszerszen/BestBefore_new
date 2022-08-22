package pl.dszerszen.bestbefore.ui.settings

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pl.dszerszen.bestbefore.ui.theme.BestBeforeTheme
import pl.dszerszen.bestbefore.ui.theme.dimens
import pl.dszerszen.bestbefore.util.StringValue
import pl.dszerszen.bestbefore.util.asStringValue

@Composable
fun SettingsSection(
    title: StringValue,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier) {
        Text(
            modifier = Modifier.padding(start = dimens.medium),
            text = title.get(),
            color = colors.onBackground
        )
        Spacer(Modifier.height(dimens.small))
        Surface(
            shape = RoundedCornerShape(dimens.medium),
            color = colors.secondary.copy(alpha = 0.2f),
        ) {
            ColumnWithDividers(
                modifier = Modifier.padding(
                    horizontal = dimens.medium,
                    vertical = dimens.small
                ),
                forceEqualHeight = true,
                divider = { Divider(color = colors.onSecondary) },
                content = content
            )
        }
    }

}

@Composable
fun SettingsRedirectButton(
    name: StringValue,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier.then(Modifier.clickable(onClick = onClick)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(name.get())
        Spacer(Modifier.weight(1f))
        Icon(Icons.Default.ArrowForward, null)
    }
}

@Composable
fun SettingsToggle(
    name: StringValue,
    isChecked: Boolean,
    modifier: Modifier = Modifier,
    onClicked: (Boolean) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(name.get())
        Spacer(Modifier.weight(1f))
        Switch(checked = isChecked, onCheckedChange = onClicked)
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light theme")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark theme")
@Composable
fun SettingsSectionPreview() {
    BestBeforeTheme(previewMode = true) {
        SettingsSection(
            title = "Section title".asStringValue()
        ) {
            repeat(3) {
                SettingsRedirectButton("Click me ${it + 1}".asStringValue()) {}
                SettingsToggle("Toggle ${it + 1}".asStringValue(), it % 2 == 0) {}
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light theme")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark theme")
@Composable
fun SettingsRedirectButtonPreview() {
    BestBeforeTheme(previewMode = true) {
        SettingsRedirectButton("Click me".asStringValue()) {}
    }
}
