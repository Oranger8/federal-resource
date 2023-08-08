package my.orange.fedresurs.service

import kotlinx.coroutines.flow.Flow
import my.orange.fedresurs.domain.Message

interface ExcelService {
    
    suspend fun export(messages: Flow<Message>)
}