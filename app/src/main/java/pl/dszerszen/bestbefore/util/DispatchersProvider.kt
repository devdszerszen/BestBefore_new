package pl.dszerszen.bestbefore.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface DispatchersProvider {
    fun ioDispatcher(): CoroutineDispatcher
    fun mainDispatcher(): CoroutineDispatcher
    fun defaultDispatcher(): CoroutineDispatcher
}

object AppDispatchersProvider: DispatchersProvider {
    override fun ioDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    override fun mainDispatcher(): CoroutineDispatcher {
        return Dispatchers.Main
    }

    override fun defaultDispatcher(): CoroutineDispatcher {
        return Dispatchers.Default
    }

}