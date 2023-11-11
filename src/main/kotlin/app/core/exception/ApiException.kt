package app.core.exception

abstract class ApiException : Exception() {
    abstract override val message: String
    abstract override val cause: Throwable?
}

class InvalidTokenException : ApiException() {
    override val message: String = "Invalid auth token"
    override val cause: Throwable? = null
}
