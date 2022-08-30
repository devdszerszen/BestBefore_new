package pl.dszerszen.bestbefore.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun nowDate(): LocalDate = LocalDate.now()

fun LocalDate.formatFullDate(): String {
    return this.format(DateTimeFormatter.ofPattern("d MMMM yyyy"))
}