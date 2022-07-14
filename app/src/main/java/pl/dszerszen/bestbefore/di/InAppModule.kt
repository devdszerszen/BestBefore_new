package pl.dszerszen.bestbefore.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.channels.Channel
import pl.dszerszen.bestbefore.ui.inapp.InAppEvent
import pl.dszerszen.bestbefore.ui.inapp.InAppEventDispatcher
import pl.dszerszen.bestbefore.ui.inapp.InAppEventHandler
import pl.dszerszen.bestbefore.util.Logger

@Module
@InstallIn(SingletonComponent::class)
object InAppModule {
    private val inAppEventsChannel = Channel<InAppEvent>(Channel.CONFLATED)

    @Provides
    fun provideInAppEventDispatcher(logger: Logger): InAppEventDispatcher {
        return object : InAppEventDispatcher {
            override fun dispatchEvent(event: InAppEvent) {
                val result = inAppEventsChannel.trySend(event)
                logger.log("INAPP: Trying to send InApp event: $event")
            }
        }
    }

    @Provides
    fun provideInAppEventHandler(logger: Logger): InAppEventHandler {
        return object : InAppEventHandler {
            override suspend fun handleEvent(onEvent: (InAppEvent) -> Unit) {
                for (event in inAppEventsChannel) {
                    logger.log("INAPP: Received InApp event: $event")
                    onEvent.invoke(event)
                }
            }
        }
    }
}