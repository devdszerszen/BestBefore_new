package pl.dszerszen.bestbefore.ui.start

import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import pl.dszerszen.bestbefore.BaseTest
import pl.dszerszen.bestbefore.TestCoroutineExtension
import pl.dszerszen.bestbefore.domain.product.interactor.AddProductsUseCase
import pl.dszerszen.bestbefore.domain.product.interactor.DeleteProductUseCase
import pl.dszerszen.bestbefore.domain.product.interactor.GetAllProductsUseCase
import pl.dszerszen.bestbefore.domain.product.model.Product
import pl.dszerszen.bestbefore.ui.inapp.InAppEvent
import pl.dszerszen.bestbefore.ui.inapp.InAppEventDispatcher
import pl.dszerszen.bestbefore.ui.main.MainScreenUiIntent
import pl.dszerszen.bestbefore.ui.main.MainViewModel
import pl.dszerszen.bestbefore.util.Logger
import pl.dszerszen.bestbefore.util.asError
import pl.dszerszen.bestbefore.util.asStringValue
import pl.dszerszen.bestbefore.util.asSuccess
import pl.dszerszen.bestbefore.withValue
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(TestCoroutineExtension::class)
internal class MainViewModelTest : BaseTest() {

    @RelaxedMockK
    private lateinit var logger: Logger

    @RelaxedMockK
    private lateinit var getAllProductsUseCase: GetAllProductsUseCase

    @RelaxedMockK
    private lateinit var inAppEventDispatcher: InAppEventDispatcher

    @RelaxedMockK
    private lateinit var deleteProductUseCase: DeleteProductUseCase

    @RelaxedMockK
    private lateinit var addProductsUseCase: AddProductsUseCase


    val eventSlot = slot<InAppEvent>()

    lateinit var sut: MainViewModel

    @BeforeEach
    fun setup() {
        eventSlot.clear()
        sut = MainViewModel(
            logger = logger,
            getAllProductsUseCase = getAllProductsUseCase,
            deleteProductUseCase = deleteProductUseCase,
            addProductsUseCase = addProductsUseCase,
            inAppEventDispatcher = inAppEventDispatcher
        )
    }

    @Test
    fun `should initially has loader and empty list`() = runTest {
        sut.viewState.withValue {
            assertEquals(true, loaderEnabled)
            assertEquals(0, allProducts.size)
        }
    }

    @Test
    fun `should hide loader and fetch products on use case response`() = runTest {
        // Arrange
        coEvery { getAllProductsUseCase.invoke() } returns flowOf((0..9).map {
            Product(
                "",
                date = LocalDate.parse("2020-01-01")
            )
        }.asSuccess())

        sut.viewState.withValue {
            assertEquals(true, loaderEnabled)
        }
        advanceUntilIdle()
        sut.viewState.withValue {
            assertEquals(false, loaderEnabled)
            assertEquals(10, allProducts.size)
        }
    }

    @Test
    fun `should set error message when use case response is error`() = runTest {
        coEvery { getAllProductsUseCase.invoke() } returns flowOf(emptyList<Product>().asError("message".asStringValue()))
        advanceUntilIdle()
        sut.viewState.withValue {
            assertEquals(false, loaderEnabled)
            assertEquals("message", errorMessage?.forceGet())
        }
    }

    @Test
    fun `should invoke delete product use case when product swiped to delete`() = runTest {
        val sampleProduct: Product = mockk(relaxed = true)
        sut.onUiIntent(MainScreenUiIntent.OnProductSwiped(sampleProduct))
        advanceUntilIdle()
        coVerify { deleteProductUseCase.invoke(sampleProduct) }
    }

    @Test
    fun `should invoke add product use case when product requested to restore`() = runTest {
        val sampleProduct: Product = mockk(relaxed = true)
        sut.onUiIntent(MainScreenUiIntent.OnRestoreRequested(sampleProduct))
        advanceUntilIdle()
        coVerify { addProductsUseCase.invoke(listOf(sampleProduct)) }
    }

    @Test
    fun `should use all products list when no search value provided`() = runTest {
        //Arrange
        coEvery { getAllProductsUseCase.invoke() } returns flowOf((0..3).map {
            Product(
                "",
                date = LocalDate.parse("2020-01-01")
            )
        }.asSuccess())
        //Act
        advanceUntilIdle()
        //Assert
        sut.viewState.withValue {
            products shouldBeSameInstanceAs allProducts
            filteredProducts.shouldBeNull()
        }
    }

    @Test
    fun `should use filtered products list when search value provided`() = runTest {
        //Arrange
        coEvery { getAllProductsUseCase.invoke() } returns flowOf((0..3).map {
            Product(
                "search$it",
                date = LocalDate.parse("2020-01-01")
            )
        }.asSuccess())
        //Act
        advanceUntilIdle()
        sut.onUiIntent(MainScreenUiIntent.OnSearchTextChanged("search0"))
        //Assert
        sut.viewState.withValue {
            products shouldBeSameInstanceAs filteredProducts
            filteredProducts?.shouldHaveSize(1)
        }
    }
}