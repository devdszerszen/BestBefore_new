package pl.dszerszen.bestbefore.util

import android.content.Context
import android.os.Parcelable
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import kotlinx.parcelize.Parcelize

sealed class StringValue : Parcelable {
    fun get(context: Context): String {
        return when (this) {
            is TextString -> text
            is ResString -> context.getString(id)
        }
    }

    @Composable
    fun get(): String {
        return when (this) {
            is TextString -> text
            is ResString -> stringResource(id)
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun forceGet(): String {
        return when (this) {
            is ResString -> throw Exception("Cannot get ResString value")
            is TextString -> text
        }
    }
}

@Parcelize
class TextString(val text: String) : StringValue()

@Parcelize
class ResString(@StringRes val id: Int): StringValue()

fun String.asStringValue(): StringValue = TextString(this)