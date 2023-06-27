package my.orange.fedresurs.domain

import java.time.LocalDateTime

data class MessagesResponse(
    val total: Int,
    val messages: List<MessageDesc>
)

data class MessageDesc(
    val guid: String,
    val number: String,
    val messageType: MessageType,
    val datePublish: LocalDateTime,
    val publisher: String,
    val participants: List<String>?,
    val bodyAttributes: List<BodyAttribute>,
    val isAnnulled: Boolean,
    val isLocked: Boolean
) {

    data class BodyAttribute(
        val number: String?,
        val date: LocalDateTime?
    )
}

data class Message(
    val guid: String,
    val number: String,
    val datePublish: LocalDateTime,
    val dateDisclosure: LocalDateTime?,
    val type: MessageType,
    val publisher: Publisher,
    val participants: List<Participant>?,
    val content: String?,
    val annulmentMessage: AnnulmentMessage?,
    val lockReason: String?,
    val filesInfo: List<FileInfo>,
    val linkedMessages: List<LinkedMessage>,
    val notaryInfo: NotaryInfo?,
    val arbitrManagerInfo: ArbitrManagerInfo?,
    val contentAdditionalInfo: ContentAdditionalInfo?
)

data class MessageType(
    val name: String,
    val description: String
)

data class Publisher(
    val type: ParticipantType,
    val data: ParticipantData
)

data class ParticipantData(
    val fullName: String?,
    val inn: String?,
    val ogrn: String?,
    val egrulAddress: String?,
    val fio: String?,
    val ogrnip: String?,
    val name: String?,
    val latinName: String?,
    val countryCodeNum: String?,
    val country: String?,
    val regNum: String?,
    val innOrAnalogue: String?
)

data class Participant(
    val role: String,
    val type: ParticipantType,
    val data: ParticipantData
)

data class AnnulmentMessage(
    val guid: String,
    val number: String,
    val datePublish: LocalDateTime,
    val type: MessageType
)

data class FileInfo(
    val guid: String,
    val name: String,
    val size: Int,
    val isDangerous: Boolean?
)

data class LinkedMessage(
    val guid: String,
    val number: String,
    val type: MessageType,
    val datePublish: LocalDateTime,
    val annulmentMessage: AnnulmentMessage?,
    val lockReason: String?,
    val contentMessageGuid: String?
)

data class NotaryInfo(
    val name: String,
    val title: String
)

data class ArbitrManagerInfo(
    val name: String
)

data class ContentAdditionalInfo(
    val companies: List<ParticipantData>?,
    val message: AnnulmentMessage?
)