package pl.dszerszen.bestbefore.util

sealed class Response<T>(open val data: T?, open val errorMessage: StringValue? = null) {
    class Success<T>(override val data: T) : Response<T>(data)
    class Loading<T>(override val data: T?) : Response<T>(data)
    class Error<T>(
        override val errorMessage: StringValue,
        override val data: T? = null
    ) : Response<T>(null, errorMessage)
}

fun <T> T.asSuccess(): Response.Success<T> = Response.Success(this)
fun <T> T.asError(message: StringValue): Response.Error<T> = Response.Error(message)