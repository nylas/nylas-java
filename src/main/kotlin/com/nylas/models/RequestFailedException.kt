package com.nylas.models

import com.nylas.util.JsonHelper.Companion.jsonToMap

/**
 * This exception represents an http response without a 2xx status code
 */
class RequestFailedException : Exception {
    /**
     * Use getStatusCode instead.
     */
    /**
     * The status code returned from the API
     */
    val status: Int
        /**
         * Use getStatusCode instead.
         */
        @Deprecated("") get() = field

    /**
     * The error message returned from the Nylas API payload
     */
    val errorMessage: String?

    /**
     * The type of error returned from the Nylas API payload
     */
    val errorType: String?

    constructor(statusCode: Int, errorMessage: String?, errorType: String?) : super(
        formatError(
            statusCode,
            errorType,
            errorMessage
        )
    ) {
        status = statusCode
        this.errorMessage = errorMessage
        this.errorType = errorType
    }

    /**
     * Use parseErrorResponse instead.
     */
    @Deprecated("")
    constructor(statusCode: Int, responseBody: String?) : super("statusCode=$statusCode") {
        status = statusCode
        var errorMessage: String? = null
        var errorType: String? = null
        if (responseBody != null && responseBody.length > 0) {
            try {
                val responseFields = jsonToMap(responseBody)
                errorMessage = responseFields["message"] as String?
                errorType = responseFields["type"] as String?
            } catch (t: Throwable) {
                // swallow
            }
        }
        this.errorMessage = errorMessage
        this.errorType = errorType
    }

    override fun toString(): String {
        return "RequestFailedException [" + formatError(status, errorType, errorMessage) + "]"
    }

    companion object {
        /**
         * Creates an error response
         * @param statusCode The status code returned from the API
         * @param responseBody The error payload to parse details from
         * @return The error with the details from the API
         */
        fun parseErrorResponse(statusCode: Int, responseBody: String?): RequestFailedException {
            var errorMessage: String? = null
            var errorType: String? = null
            if (!responseBody.isNullOrEmpty()) {
                try {
                    val responseFields = jsonToMap(responseBody)
                    errorMessage = responseFields["message"] as String?
                    errorType = responseFields["type"] as String?
                } catch (t: Throwable) {
                    // swallow
                }
            }
            return RequestFailedException(statusCode, errorMessage, errorType)
        }

        protected fun formatError(statusCode: Int, type: String?, message: String?): String {
            return "statusCode=$statusCode, type=$type, message=$message"
        }
    }
}
