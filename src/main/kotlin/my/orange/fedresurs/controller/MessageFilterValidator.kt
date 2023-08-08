package my.orange.fedresurs.controller

import my.orange.fedresurs.domain.MessageFilter
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class MessageFilterValidator : Validator<MessageFilter> {
    
    override fun validate(value: MessageFilter) {
        with(value) {
            if (listOf(number, bodyAttribute, participant.code).all { it == null }) {
                requireNotNull(dateBegin)
                requireNotNull(dateEnd)
                require(dateEnd!!.isAfter(dateBegin))
                val duration = Duration.between(dateBegin, dateEnd)
                require(duration >= Duration.ofSeconds(1) && duration <= Duration.ofDays(31))
                participant.code?.let { requireNotNull(participant.type) }
                participant.type?.let { requireNotNull(participant.code) }
            }
        }
    }
}