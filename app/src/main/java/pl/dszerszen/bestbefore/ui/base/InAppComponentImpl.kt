package pl.dszerszen.bestbefore.ui.base

import kotlinx.coroutines.channels.Channel
import pl.dszerszen.bestbefore.ui.inapp.InAppEvent
import pl.dszerszen.bestbefore.ui.inapp.InAppEventDispatcher
import pl.dszerszen.bestbefore.ui.inapp.InAppEventHandler

object InAppComponentImpl : InAppEventDispatcher, InAppEventHandler {
    private val inAppEventsChannel = Channel<InAppEvent>(Channel.CONFLATED)

    override fun dispatchEvent(event: InAppEvent) {
        inAppEventsChannel.trySend(event)
    }

    override suspend fun handleEvent(onEvent: (InAppEvent) -> Unit) {
        for (event in inAppEventsChannel) {
            onEvent.invoke(event)
        }
    }
}