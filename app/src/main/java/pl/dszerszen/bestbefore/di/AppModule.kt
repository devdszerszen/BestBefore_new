package pl.dszerszen.bestbefore.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pl.dszerszen.bestbefore.data.product.local.ProductsDatabase
import pl.dszerszen.bestbefore.util.AppDispatchersProvider
import pl.dszerszen.bestbefore.util.DebugLogger
import pl.dszerszen.bestbefore.util.DispatchersProvider
import pl.dszerszen.bestbefore.util.Logger
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLogger(): Logger {
        return DebugLogger
    }

    @Provides
    @Singleton
    fun provideDispatcher(): DispatchersProvider {
        return AppDispatchersProvider
    }

    @Provides
    @Singleton
    fun provideProductsDatabase(app: Application): ProductsDatabase {
        return Room.databaseBuilder(
            app,
            ProductsDatabase::class.java,
            "productsdb.db"
        ).build()
    }
}