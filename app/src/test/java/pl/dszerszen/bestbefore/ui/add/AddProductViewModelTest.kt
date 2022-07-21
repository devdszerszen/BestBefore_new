package pl.dszerszen.bestbefore.ui.add

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import pl.dszerszen.bestbefore.TestCoroutineExtension
import pl.dszerszen.bestbefore.domain.config.ConfigRepository
import pl.dszerszen.bestbefore.domain.config.model.GlobalConfig
import pl.dszerszen.bestbefore.ui.add.ScannerStatus.*
import pl.dszerszen.bestbefore.ui.inapp.RequestPermissionUseCase
import pl.dszerszen.bestbefore.util.Logger
import pl.dszerszen.bestbefore.withValue

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(TestCoroutineExtension::class)
internal class AddProductViewModelTest {

    private val logger: Logger = mockk(relaxed = true)
    private val requestPermissionUseCase: RequestPermissionUseCase = mockk(relaxed = true)
    private val configRepository: ConfigRepository = mockk(relaxed = true)

    lateinit var sut: AddProductViewModel

    private fun setupSut(
        scannerEnabledInGlobalConfig: Boolean = true,
        cameraPermissionEnabled: Boolean = true
    ) {
        coEvery { requestPermissionUseCase.invoke(any()) } returns cameraPermissionEnabled
        every { configRepository.getConfig() } returns
                GlobalConfig().copy(isBarcodeScannerEnabled = scannerEnabledInGlobalConfig)
        sut = AddProductViewModel(logger, requestPermissionUseCase, configRepository)
    }

    @Test
    fun `should have enabled barcode scanner when camera permission granted`() = runTest {
        //Arrange
        setupSut()
        //Act
        advanceUntilIdle()
        //Assert
        sut.viewState.withValue {
            assertEquals(true, canShowScanner())
            assertEquals(READY_TO_SCAN, scannerStatus)
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
            assertEquals(false, canShowScanner())
            assertEquals(DISABLED, scannerStatus)
        }
    }

    @Test
    fun `should check camera permission when barcode scanner enabled in global config`() = runTest {
        //Arrange
        setupSut()
        //Act
        advanceUntilIdle()
        //Assert
        coVerify(exactly = 1) { requestPermissionUseCase.invoke(any()) }

    }

    @Test
    fun `should not check camera permission when scanner disabled in global config`() = runTest {
        //Arrange
        setupSut(false)
        //Act
        //Assert
        coVerify(exactly = 0) { requestPermissionUseCase.invoke(any()) }
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
            assertEquals(null, barcode)
            assertEquals(READY_TO_SCAN, scannerStatus)
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
            assertEquals(SAMPLE_BARCODE, barcode)
            assertEquals(SCANNED_SUCCESSFULLY, scannerStatus)
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
            assertEquals(DISMISSED, scannerStatus)
        }
    }

    companion object {
        const val SAMPLE_BARCODE = "1234"
    }
}