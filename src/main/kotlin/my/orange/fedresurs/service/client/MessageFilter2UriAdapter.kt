package my.orange.fedresurs.service.client

import my.orange.fedresurs.domain.MessageFilter
import org.springframework.web.util.UriBuilder

class MessageFilter2UriAdapter : (UriBuilder, MessageFilter) -> UriBuilder {

    override fun invoke(builder: UriBuilder, filter: MessageFilter) = with(filter) {
        dateBegin?.let { builder.queryParam("dateBegin", it) }
        dateEnd?.let { builder.queryParam("dateEnd", it) }
        number?.let { builder.queryParam("number", it) }
        messageTypes.takeIf { it.isNotEmpty() }?.let { builder.queryParam("messageTypes", it) }
        bodyAttribute?.let { builder.queryParam("bodyAttribute", it) }
        participant.type?.let { builder.queryParam("participant.type", it) }
        participant.code?.let { builder.queryParam("participant.code", it) }
        builder
    }
}