package pl.dszerszen.bestbefore

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.extension.*
import pl.dszerszen.bestbefore.util.DispatchersProvider

@ExperimentalCoroutinesApi
class TestCoroutineExtension : BeforeEachCallback, AfterEachCallback {
    override fun beforeEach(context: ExtensionContext?) {
        Dispatchers.setMain(TestDispatchersProvider.mainDispatcher())
    }

    override fun afterEach(context: ExtensionContext?) {
        Dispatchers.resetMain()
    }

}

@Suppress("DEPRECATION")
@ExperimentalCoroutinesApi
object TestDispatchersProvider : DispatchersProvider {
    private val dispatcher = TestCoroutineDispatcher()
    override fun mainDispatcher() = dispatcher
    override fun ioDispatcher() = dispatcher
    override fun defaultDispatcher() = dispatcher
}