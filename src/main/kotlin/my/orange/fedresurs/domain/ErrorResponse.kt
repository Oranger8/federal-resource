package my.orange.fedresurs.domain

data class ErrorResponse(val code: Int = -1, val message: String = "unknown error")