package pl.dszerszen.bestbefore.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pl.dszerszen.bestbefore.ui.base.InAppComponentImpl
import pl.dszerszen.bestbefore.ui.inapp.InAppEventDispatcher
import pl.dszerszen.bestbefore.ui.inapp.InAppEventHandler
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InAppModule {

    @Provides
    @Singleton
    fun provideInAppEventDispatcher(): InAppEventDispatcher {
        return InAppComponentImpl
    }

    @Provides
    @Singleton
    fun provideInAppEventHandler(): InAppEventHandler {
        return InAppComponentImpl
    }
}