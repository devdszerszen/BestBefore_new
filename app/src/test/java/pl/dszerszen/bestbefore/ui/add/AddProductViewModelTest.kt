package pl.dszerszen.bestbefore.ui.add

import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import pl.dszerszen.bestbefore.TestCoroutineExtension
import pl.dszerszen.bestbefore.domain.config.ConfigRepository
import pl.dszerszen.bestbefore.domain.config.model.GlobalConfig
import pl.dszerszen.bestbefore.ui.add.ScannerStatus.*
import pl.dszerszen.bestbefore.ui.inapp.InAppEventDispatcher
import pl.dszerszen.bestbefore.ui.inapp.requestPermission
import pl.dszerszen.bestbefore.util.Logger
import pl.dszerszen.bestbefore.withValue

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(TestCoroutineExtension::class, MockKExtension::class)
internal class AddProductViewModelTest {

    @RelaxedMockK
    private lateinit var logger: Logger

    @RelaxedMockK
    private lateinit var inAppEventDispatcher: InAppEventDispatcher

    @RelaxedMockK
    private lateinit var configRepository: ConfigRepository

    lateinit var sut: AddProductViewModel

    private fun setupSut(
        scannerEnabledInGlobalConfig: Boolean = true,
        cameraPermissionEnabled: Boolean = true
    ) {
        mockkStatic(InAppEventDispatcher::requestPermission)
        coEvery { inAppEventDispatcher.requestPermission(any()) } returns cameraPermissionEnabled
        every { configRepository.getConfig() } returns
                GlobalConfig().copy(isBarcodeScannerEnabled = scannerEnabledInGlobalConfig)
        sut = AddProductViewModel(logger, inAppEventDispatcher, configRepository)
    }

    @Test
    fun `should have enabled barcode scanner when camera permission granted`() = runTest {
        //Arrange
        setupSut()
        //Act
        advanceUntilIdle()
        //Assert
        sut.viewState.withValue {
            canShowScanner().shouldBeTrue()
            scannerStatus shouldBe READY_TO_SCAN
        }
    }

    @Test
    fun `should have disabled barcode scanner when no camera permission`() = runTest {
        //Arrange
        setupSut(cameraPermissionEnabled = false)
        //Act
        advanceUntilIdle()
        //Assert
        sut.viewState.withValue {
            canShowScanner().shouldBeFalse()
            scannerStatus shouldBe DISABLED
        }
    }

    @Test
    fun `should check camera permission when barcode scanner enabled in global config`() = runTest {
        //Arrange
        setupSut()
        //Act
        advanceUntilIdle()
        //Assert
        coVerify(exactly = 1) { inAppEventDispatcher.requestPermission(any()) }

    }

    @Test
    fun `should not check camera permission when scanner disabled in global config`() = runTest {
        //Arrange
        setupSut(false)
        //Act
        //Assert
        coVerify(exactly = 0) { inAppEventDispatcher.requestPermission(any()) }
    }

    @Test
    fun `should NOT update state when no barcode scanned`() = runTest {
        //Arrange
        setupSut()
        //Act
        advanceUntilIdle()
        sut.onBarcodeScanned(emptyList())
        //Assert
        sut.viewState.withValue {
            barcode.shouldBeNull()
            scannerStatus shouldBe READY_TO_SCAN
        }
    }

    @Test
    fun `should update state and set scanned barcode after successfull scan`() = runTest {
        //Arrange
        setupSut()
        //Act
        advanceUntilIdle()
        sut.onBarcodeScanned(listOf(SAMPLE_BARCODE))
        //Assert
        sut.viewState.withValue {
            barcode shouldBe SAMPLE_BARCODE
            scannerStatus shouldBe SCANNED_SUCCESSFULLY
        }
    }

    @Test
    fun `should dismiss scanner on scanned close clicked`() = runTest {
        //Arrange
        setupSut()
        //Act
        advanceUntilIdle()
        sut.onBarcodeScannerClosed()
        //Assert
        sut.viewState.withValue {
            scannerStatus shouldBe DISMISSED
        }
    }

    companion object {
        const val SAMPLE_BARCODE = "1234"
    }
}