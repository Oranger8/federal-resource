package my.orange.fedresurs.service

import kotlinx.coroutines.flow.Flow
import my.orange.fedresurs.domain.Message
import my.orange.fedresurs.domain.MessageFilter
import my.orange.fedresurs.domain.MessagesResponse
import my.orange.fedresurs.domain.Pageable

interface FederalResourceService {
    
    suspend fun getMessages(filter: MessageFilter, pageable: Pageable): MessagesResponse
    suspend fun getMessage(guid: String): Message?
    fun export(filter: MessageFilter, pageable: Pageable = Pageable(null, 100, 0)): Flow<Message>
}

