package pl.dszerszen.bestbefore

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import pl.dszerszen.bestbefore.util.Logger

@ExperimentalCoroutinesApi
@ExtendWith(TestCoroutineExtension::class)
internal class MainViewModelTest {

    private lateinit var sut: MainViewModel

    private val logger: Logger = mockk(relaxed = true)
    private val useCase: InitializeUseCase = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        coEvery { useCase.invoke() } returns true
        sut = MainViewModel(logger, useCase)
    }

    @Test
    fun `sample test at init block`() = runTest {

        coVerify(exactly = 1) { useCase.invoke() }
        assertEquals(true, sut.isInitialized)

    }
}