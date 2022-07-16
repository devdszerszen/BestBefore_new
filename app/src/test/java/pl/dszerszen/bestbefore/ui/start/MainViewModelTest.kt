package pl.dszerszen.bestbefore.ui.start

import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import pl.dszerszen.bestbefore.TestCoroutineExtension
import pl.dszerszen.bestbefore.domain.product.interactor.GetAllProductsUseCase
import pl.dszerszen.bestbefore.domain.product.model.Product
import pl.dszerszen.bestbefore.ui.inapp.InAppEvent
import pl.dszerszen.bestbefore.ui.inapp.InAppEventDispatcher
import pl.dszerszen.bestbefore.ui.main.MainViewModel
import pl.dszerszen.bestbefore.util.Logger
import pl.dszerszen.bestbefore.util.Response
import pl.dszerszen.bestbefore.util.asStringValue
import pl.dszerszen.bestbefore.util.asSuccess
import pl.dszerszen.bestbefore.withValue

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(TestCoroutineExtension::class)
internal class MainViewModelTest {

    private val logger: Logger = mockk(relaxed = true)
    private val getAllProductsUseCase: GetAllProductsUseCase = mockk(relaxed = true)
    private val inAppEventDispatcher: InAppEventDispatcher = mockk(relaxed = true)
    val eventSlot = slot<InAppEvent>()

    lateinit var sut: MainViewModel

    @BeforeEach
    fun setup() {
        eventSlot.clear()
        sut = MainViewModel(logger, getAllProductsUseCase, inAppEventDispatcher)
    }

    @Test
    fun `should initially has loader and empty list`() = runTest {
        sut.viewState.withValue {
            assertEquals(true, loaderEnabled)
            assertEquals(0, products.size)
        }
    }

    @Test
    fun `should hide loader and fetch products on use case response`() = runTest {
        coEvery { getAllProductsUseCase.invoke() } returns (0..9).map { Product("") }.asSuccess()

        sut.viewState.withValue {
            assertEquals(true, loaderEnabled)
        }
        sut.initialize()
        advanceUntilIdle()
        sut.viewState.withValue {
            assertEquals(false, loaderEnabled)
            assertEquals(10, products.size)
        }
    }

    @Test
    fun `should set error message when use case response is error`() = runTest {
        coEvery { getAllProductsUseCase.invoke() } returns Response.Error("Sample error".asStringValue())

        sut.initialize()
        advanceUntilIdle()
        sut.viewState.withValue {
            assertEquals(false, loaderEnabled)
            assertEquals("Sample error", errorMessage?.forceGet())
        }
    }

    @Test
    fun `should navigate to settings on button clicked`() {
        sut.onButtonClick()
        verify(exactly = 1) { inAppEventDispatcher.dispatchEvent(capture(eventSlot)) }
        assertInstanceOf(InAppEvent.Navigate::class.java, eventSlot.captured)
    }
}