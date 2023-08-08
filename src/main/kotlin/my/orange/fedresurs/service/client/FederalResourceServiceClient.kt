package my.orange.fedresurs.service.client

import io.netty.handler.codec.http.HttpResponseStatus.TOO_MANY_REQUESTS
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import my.orange.fedresurs.config.FederalResourceProperties
import my.orange.fedresurs.domain.*
import my.orange.fedresurs.flatMap
import my.orange.fedresurs.logger
import my.orange.fedresurs.service.FederalResourceService
import my.orange.fedresurs.service.PasswordEncryptor
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.reactive.function.client.bodyToMono
import org.springframework.web.util.UriBuilder
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.onErrorMap
import reactor.util.retry.Retry
import java.time.Duration

@Service
class FederalResourceServiceClient(
    properties: FederalResourceProperties,
    clientBuilder: WebClient.Builder,
    private val passwordEncryptor: PasswordEncryptor
) : FederalResourceService {

    private val logger = logger()

    private val client = clientBuilder.baseUrl(properties.url).build()
    private val tooManyRequests = Retry.fixedDelay(1, Duration.ofSeconds(1))
        .filter { it is WebClientResponseException && it.statusCode == TOO_MANY_REQUESTS }
    private val notFoundPredicate = { throwable: Throwable ->
        throwable is WebClientResponseException && throwable.statusCode == NOT_FOUND
    }
    private val throwable2EmptyMapper = { _: Throwable -> Mono.empty<Message>() }
    private val messageFilter2UriAdapter = MessageFilter2UriAdapter()
    private val pageable2UriAdapter = Pageable2UriAdapter()
    private val responseErrorMapper = ResponseErrorMapper()

    private val tokenMono = client.post()
        .uri("/v1/auth")
        .bodyValue(AuthRequest(properties.login, properties.password.encrypted()))
        .retrieve()
        .bodyToMono<AuthResponse>()
        .retryWhen(tooManyRequests)
        .onErrorMap(WebClientResponseException::class, responseErrorMapper)
        .doOnNext { logger.info("Token received: {}", it?.jwt) }
        .cacheForHalfADayOnSuccess()
        .delayElement(Duration.ofMillis(150))

    override suspend fun getMessages(filter: MessageFilter, pageable: Pageable): MessagesResponse {
        val token = tokenMono.awaitSingle().jwt
        return client.get()
            .uri {
                it.path("/v1/messages")
                    .applyFilter(filter)
                    .applyPageable(pageable)
                    .build()
            }
            .headers { it.setBearerAuth(token) }
            .retrieve()
            .bodyToMono<MessagesResponse>()
            .retryWhen(tooManyRequests)
            .onErrorMap(WebClientResponseException::class, responseErrorMapper)
            .delayElement(Duration.ofMillis(150))
            .awaitSingle()
    }

    override suspend fun getMessage(guid: String): Message? {
        val token = tokenMono.awaitSingle().jwt
        return client.get()
            .uri("/v1/messages/{guid}", guid)
            .headers { it.setBearerAuth(token) }
            .retrieve()
            .bodyToMono<Message>()
            .retryWhen(tooManyRequests)
            .onErrorResume(notFoundPredicate, throwable2EmptyMapper)
            .onErrorMap(WebClientResponseException::class, responseErrorMapper)
            .delayElement(Duration.ofMillis(150))
            .awaitSingleOrNull()
    }

    override fun export(filter: MessageFilter, pageable: Pageable): Flow<Message> = filter.by30Days()
        .flatMap {
            val (total, messages) = getMessages(it, pageable)
            logger.info("Found ${messages.size} messages")
            messages.asFlow()
                .mapNotNull { messageDesc -> getMessage(messageDesc.guid) }
                .onEach { message -> logger.info("Received message ${message.guid}") }
                .onCompletion { throwable ->
                    throwable?.let { th -> logger.error("Error: ${th.message}", th) }
                    if (throwable == null && pageable.hasNext(total)) {
                        emitAll(export(it, pageable.next()))
                    }
                }
        }

    fun MessageFilter.by30Days(): Flow<MessageFilter> {
        if (dateBegin == null || dateEnd == null) return emptyFlow()
        return flow {
            if (Duration.between(dateBegin, dateEnd) > Duration.ofDays(30)) {
                val nextDate = dateBegin!!.plusDays(30)
                logger.info("Looking between $dateBegin - $nextDate")
                emit(copy(dateEnd = nextDate))
                emitAll(copy(dateBegin = nextDate).by30Days())
            } else {
                logger.info("Looking between $dateBegin - $dateEnd")
                emit(this@by30Days)
            }
        }
    }

    private fun <T> Mono<T>.cacheForHalfADayOnSuccess() =
        cache({ Duration.ofHours(12) }, { Duration.ZERO }, { Duration.ZERO })

    private fun String.encrypted() = passwordEncryptor.encrypt(this)

    private fun UriBuilder.applyFilter(filter: MessageFilter) = messageFilter2UriAdapter(this, filter)

    private fun UriBuilder.applyPageable(pageable: Pageable) = pageable2UriAdapter(this, pageable)
}