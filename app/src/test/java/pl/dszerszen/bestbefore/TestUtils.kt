package pl.dszerszen.bestbefore

import kotlinx.coroutines.flow.StateFlow

fun <T>StateFlow<T>.withValue(action: T.() -> Unit) {
    action(this.value)
}