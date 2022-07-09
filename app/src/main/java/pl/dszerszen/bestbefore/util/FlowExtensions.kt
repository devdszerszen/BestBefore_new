package pl.dszerszen.bestbefore.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

fun <T> MutableStateFlow<T>.asImmutable(): StateFlow<T> = this