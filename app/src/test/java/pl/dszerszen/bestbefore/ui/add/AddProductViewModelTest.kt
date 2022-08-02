package pl.dszerszen.bestbefore.ui.add

import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import pl.dszerszen.bestbefore.BaseTest
import pl.dszerszen.bestbefore.domain.config.ConfigRepository
import pl.dszerszen.bestbefore.domain.config.model.GlobalConfig
import pl.dszerszen.bestbefore.ui.add.AddProductUiIntent.BarcodeScanned
import pl.dszerszen.bestbefore.ui.add.AddProductUiIntent.ScannerClosed
import pl.dszerszen.bestbefore.ui.inapp.InAppEventDispatcher
import pl.dszerszen.bestbefore.ui.inapp.requestPermission
import pl.dszerszen.bestbefore.util.Logger
import pl.dszerszen.bestbefore.withValue

@OptIn(ExperimentalCoroutinesApi::class)
internal class AddProductViewModelTest : BaseTest() {

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
            canUseScanner.shouldBeTrue()
            isDuringScanning.shouldBeTrue()
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
            canUseScanner.shouldBeFalse()
            isDuringScanning.shouldBeFalse()
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
    fun `should NOT check camera permission when scanner disabled in global config`() = runTest {
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
        sut.onUiIntent(BarcodeScanned(emptyList()))
        //Assert
        sut.viewState.withValue {
            scannedBarcode.shouldBeNull()
            isDuringScanning.shouldBeTrue()
        }
    }

    @Test
    fun `should update state and set scanned barcode after successful scan`() = runTest {
        //Arrange
        setupSut()
        //Act
        advanceUntilIdle()
        sut.onUiIntent(BarcodeScanned(listOf(SAMPLE_BARCODE)))
        //Assert
        sut.viewState.withValue {
            scannedBarcode shouldBe SAMPLE_BARCODE
            isDuringScanning.shouldBeFalse()
        }
    }

    @Test
    fun `should dismiss scanner on scanned close clicked`() = runTest {
        //Arrange
        setupSut()
        //Act
        advanceUntilIdle()
        sut.onUiIntent(ScannerClosed)
        //Assert
        sut.viewState.withValue {
            isDuringScanning.shouldBeFalse()
        }
    }

    companion object {
        const val SAMPLE_BARCODE = "1234"
    }
}