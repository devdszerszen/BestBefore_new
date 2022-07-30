package pl.dszerszen.bestbefore.ui.common

import android.view.LayoutInflater
import android.widget.DatePicker
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import pl.dszerszen.bestbefore.R
import java.time.LocalDate

@Composable
fun DatePicker(
    onDateSelected: (LocalDate) -> Unit
) {
    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = { context ->
            val view = LayoutInflater.from(context).inflate(R.layout.date_picker, null)
            val datePicker = view.findViewById<DatePicker>(R.id.datePicker)
            val date = LocalDate.now()
            datePicker.init(
                date.year,
                date.monthValue-1,
                date.dayOfMonth
            ) { _, year, monthOfYear, dayOfMonth ->
                onDateSelected(LocalDate.of(year, monthOfYear+1, dayOfMonth))
            }
            datePicker
        }
    )
}