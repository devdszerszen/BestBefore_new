package pl.dszerszen.bestbefore.ui.inapp

import pl.dszerszen.bestbefore.ui.navigation.NavScreen
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

sealed class InAppEvent {
    class Navigate(val target: NavScreen) : InAppEvent()
    object NavigateBack : InAppEvent()
    class RequestPermission(val permissionName: String, val isGrantedCallback: (Boolean) -> Unit) : InAppEvent()
    class Message(val message: InAppMessage) : InAppEvent()
}

interface InAppEventDispatcher {
    fun dispatchEvent(event: InAppEvent)
}

interface InAppEventHandler {
    suspend fun handleEvent(onEvent: (InAppEvent) -> Unit)
}

fun InAppEventDispatcher.navigate(target: NavScreen) = dispatchEvent(InAppEvent.Navigate(target))
fun InAppEventDispatcher.navigateBack() = dispatchEvent(InAppEvent.NavigateBack)
fun InAppEventDispatcher.showMessage(message: InAppMessage) = dispatchEvent(InAppEvent.Message(message))

suspend fun InAppEventDispatcher.requestPermission(permissionName: String): Boolean =
    suspendCoroutine { continuation ->
        dispatchEvent(InAppEvent.RequestPermission(permissionName) { hasPermisison ->
            continuation.resume(hasPermisison)
        })
    }