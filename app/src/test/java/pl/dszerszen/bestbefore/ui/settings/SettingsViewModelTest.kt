package pl.dszerszen.bestbefore.ui.settings

import io.kotest.matchers.shouldBe
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import pl.dszerszen.bestbefore.BaseTest
import pl.dszerszen.bestbefore.TestCoroutineExtension
import pl.dszerszen.bestbefore.ui.inapp.InAppEvent
import pl.dszerszen.bestbefore.ui.inapp.InAppEventDispatcher
import pl.dszerszen.bestbefore.ui.navigation.NavScreen
import pl.dszerszen.bestbefore.util.Logger

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(TestCoroutineExtension::class)
internal class SettingsViewModelTest : BaseTest() {

    @RelaxedMockK
    private lateinit var logger: Logger

    @RelaxedMockK
    private lateinit var inAppEventDispatcher: InAppEventDispatcher


    val eventSlot = slot<InAppEvent>()

    lateinit var sut: SettingsViewModel

    @BeforeEach
    fun setup() {
        eventSlot.clear()
        sut = SettingsViewModel(
            logger = logger,
            inAppEventDispatcher = inAppEventDispatcher
        )
    }

    @Test
    fun `should navigate to categories screen when clicked manage categories`() = runTest {
        //Arrange
        //Act
        sut.onUiIntent(SettingsScreenUiIntent.OnGoToCategoriesClicked)
        advanceUntilIdle()
        //Assert
        verify { inAppEventDispatcher.dispatchEvent(capture(eventSlot)) }
        (eventSlot.captured as InAppEvent.Navigate).target shouldBe NavScreen.Categories
    }
}