package pl.dszerszen.bestbefore.util

import android.util.Log

interface Logger {
    fun log(message: String)
    fun <T> T.alsoLog(message: T.() -> String): T
}

object DebugLogger : Logger {
    private const val LOG_TAG = "DAMIAN"

    override fun log(message: String) {
        Log.d(LOG_TAG, message)
    }

    override fun <T> T.alsoLog(message: T.() -> String): T {
        log(message(this))
        return this
    }
}