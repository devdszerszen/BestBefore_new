package pl.dszerszen.bestbefore.ui.inapp

import pl.dszerszen.bestbefore.ui.navigation.NavScreen
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

sealed class InAppEvent {
    class Navigate(val target: NavScreen) : InAppEvent()
    class RequestPermission(val permissionName: String, val isGrantedCallback: (Boolean) -> Unit) : InAppEvent()
    class ShowToast(val message: String) : InAppEvent()
}

interface InAppEventDispatcher {
    fun dispatchEvent(event: InAppEvent)
}

interface InAppEventHandler {
    suspend fun handleEvent(onEvent: (InAppEvent) -> Unit)
}

class RequestPermissionUseCase @Inject constructor(
    private val inAppEventDispatcher: InAppEventDispatcher
) {
    suspend operator fun invoke(permissionName: String): Boolean {
        return suspendCoroutine { continuation ->
            inAppEventDispatcher.dispatchEvent(InAppEvent.RequestPermission(permissionName) { result ->
                continuation.resume(result)
            })
        }
    }
}

fun InAppEventDispatcher.navigate(target: NavScreen) = dispatchEvent(InAppEvent.Navigate(target))