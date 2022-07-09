package pl.dszerszen.bestbefore.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pl.dszerszen.bestbefore.data.product.ProductRepositoryFakeImpl
import pl.dszerszen.bestbefore.domain.product.ProductRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindProductsRepository(repository: ProductRepositoryFakeImpl): ProductRepository
}