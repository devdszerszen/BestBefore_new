package pl.dszerszen.bestbefore.ui.navigation

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.channels.Channel
import pl.dszerszen.bestbefore.util.Logger

private object Navigation {
    val inAppEventsChannel = Channel<NavScreen>()
}

interface NavigationDispatcher {
    fun navigate(target: NavScreen)
}

interface NavigationHandler {
    suspend fun handleEvent(actionOnEvent: (NavScreen) -> Unit)
}

@Module
@InstallIn(SingletonComponent::class)
object NavigationModule {
    @Provides
    fun provideNavigationProducer(logger: Logger): NavigationDispatcher {
        return object : NavigationDispatcher {
            override fun navigate(target: NavScreen) {
                val result = Navigation.inAppEventsChannel.trySend(target)
                logger.log("NAV: Trying to send navigation event, result: $result")
            }
        }
    }

    @Provides
    fun provideNavigationHandler(logger: Logger): NavigationHandler {
        return object : NavigationHandler {
            override suspend fun handleEvent(actionOnEvent: (NavScreen) -> Unit) {
                for (event in Navigation.inAppEventsChannel) {
                    logger.log("NAV: Received navigation event: $event")
                    actionOnEvent(event)
                }
            }
        }
    }
}