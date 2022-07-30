package pl.dszerszen.bestbefore

import io.mockk.junit5.MockKExtension
import io.mockk.mockkStatic
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import pl.dszerszen.bestbefore.ui.inapp.InAppEventDispatcher
import pl.dszerszen.bestbefore.ui.inapp.requestPermission
import pl.dszerszen.bestbefore.util.nowDate

@ExtendWith(TestCoroutineExtension::class, MockKExtension::class)
abstract class BaseTest {

    @BeforeEach
    private fun prepareTests() {
        mockkStatic(InAppEventDispatcher::requestPermission)
        mockkStatic(::nowDate)

    }
}