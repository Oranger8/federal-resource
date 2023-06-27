package my.orange.fedresurs.domain

import java.time.LocalDateTime

data class MessageFilter(
    val dateBegin: LocalDateTime? = null,
    val dateEnd: LocalDateTime? = null,
    val number: String? = null,
    val messageTypes: List<String> = emptyList(),
    val bodyAttribute: String? = null,
    val participant: Participant = Participant()
) {
    
    data class Participant(
        val type: ParticipantType? = null,
        val code: String? = null
    )
}
