package my.orange.fedresurs.controller

import my.orange.fedresurs.domain.MessageFilter
import my.orange.fedresurs.domain.MessagesResponse
import my.orange.fedresurs.domain.Pageable
import my.orange.fedresurs.service.FederalResourceService
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/messages")
class MessageController(
    private val service: FederalResourceService,
    private val messageFilterValidator: Validator<MessageFilter>,
    private val pageableValidator: Validator<Pageable>
) {

    @GetMapping
    suspend fun getMessages(filter: MessageFilter, pageable: Pageable): MessagesResponse? {
        messageFilterValidator.validate(filter)
        pageableValidator.validate(pageable)
        return service.getMessages(filter, pageable)
    }

    @GetMapping("/{guid}")
    suspend fun getMessage(@PathVariable guid: String) = service.getMessage(guid) 
        ?: throw ResponseStatusException(NOT_FOUND)
}