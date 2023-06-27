package my.orange.fedresurs.service

import my.orange.fedresurs.domain.Message
import my.orange.fedresurs.domain.MessageFilter
import my.orange.fedresurs.domain.MessagesResponse
import my.orange.fedresurs.domain.Pageable

interface FederalResourceService {
    
    suspend fun getMessages(filter: MessageFilter, pageable: Pageable): MessagesResponse?
    suspend fun getMessage(guid: String): Message?
}

