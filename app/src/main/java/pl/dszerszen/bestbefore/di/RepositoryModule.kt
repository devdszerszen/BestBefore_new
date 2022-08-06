package pl.dszerszen.bestbefore.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pl.dszerszen.bestbefore.data.config.ConfigRepositoryImpl
import pl.dszerszen.bestbefore.data.product.ProductRepositoryImpl
import pl.dszerszen.bestbefore.domain.config.ConfigRepository
import pl.dszerszen.bestbefore.domain.product.ProductRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProductsRepository(repository: ProductRepositoryImpl): ProductRepository

    @Binds
    @Singleton
    abstract fun bindConfigRepository(repository: ConfigRepositoryImpl): ConfigRepository
}