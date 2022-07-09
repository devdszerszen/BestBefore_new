package pl.dszerszen.bestbefore.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pl.dszerszen.bestbefore.util.AppDispatchersProvider
import pl.dszerszen.bestbefore.util.DebugLogger
import pl.dszerszen.bestbefore.util.DispatchersProvider
import pl.dszerszen.bestbefore.util.Logger

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideLogger(): Logger {
        return DebugLogger
    }

    @Provides
    fun provideDispatcher(): DispatchersProvider {
        return AppDispatchersProvider
    }
}