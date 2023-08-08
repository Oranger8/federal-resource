package my.orange.fedresurs.domain

import java.time.LocalDateTime

data class MessageFilter(
    var dateBegin: LocalDateTime? = null,
    var dateEnd: LocalDateTime? = null,
    var number: String? = null,
    var messageTypes: List<String> = emptyList(),
    var bodyAttribute: String? = null,
    var participant: Participant = Participant()
) {
    
    data class Participant(
        var type: ParticipantType? = null,
        var code: String? = null
    )
}
