package my.orange.fedresurs.service.client

import my.orange.fedresurs.domain.ErrorResponse
import my.orange.fedresurs.domain.FederalResourceException
import org.springframework.web.reactive.function.client.WebClientResponseException

class ResponseErrorMapper : (WebClientResponseException) -> Throwable {

    override fun invoke(exception: WebClientResponseException): Throwable {
        val (code, message) = exception.getResponseBodyAs<ErrorResponse>() ?: ErrorResponse()
        return FederalResourceException(code, message)
    }

    private inline fun <reified E> WebClientResponseException.getResponseBodyAs() = getResponseBodyAs(E::class.java)
}