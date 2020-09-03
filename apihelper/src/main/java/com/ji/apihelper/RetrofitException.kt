package com.ji.apihelper

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException

class RetrofitException(
    message: String,
    throwable: Throwable?,
    val kind: Kind,
    val retrofit: Retrofit? = null,
    val response: Response<*>? = null) : RuntimeException(message, throwable) {

    enum class Kind {
        /** An [IOException] occurred while communicating to the server.  */
        NETWORK,

        /** A non-200 HTTP status code was received from the server.  */
        HTTP,

        /**
         * An internal error occurred while attempting to execute a request. It is best practice to
         * re-throw this exception so your application crashes.
         */
        UNEXPECTED
    }

    /**
     * HTTP response body converted to specified `type`. `null` if there is no
     * response.
     *
     * @throws IOException if unable to convert the body to the specified `type`.
     */
    @Throws(IOException::class)
    fun <T> getErrorBodyAs(type: Class<T>): T? {
        response?.also {
            val converter: Converter<ResponseBody, T> = retrofit?.responseBodyConverter(type, arrayOfNulls(0))
                    ?: return null

            return converter.convert(it.errorBody())
        }

        return null
    }

    companion object {
        fun networkError(exception: IOException): RetrofitException {
            return RetrofitException(exception.message ?: "", exception, Kind.NETWORK)
        }

        fun httpError(response: Response<*>?, retrofit: Retrofit?): RetrofitException {
            return RetrofitException(response?.message() ?: "", null, Kind.HTTP, retrofit, response)
        }

        fun unExpectedError(throwable: Throwable): RetrofitException {
            return RetrofitException(throwable.message ?: "", throwable, Kind.UNEXPECTED)
        }
    }
}