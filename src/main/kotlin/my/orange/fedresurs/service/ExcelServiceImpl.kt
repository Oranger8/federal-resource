package my.orange.fedresurs.service

import kotlinx.coroutines.flow.*
import my.orange.fedresurs.domain.*
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import kotlin.io.path.Path
import kotlin.io.path.outputStream

@Service
class ExcelServiceImpl : ExcelService {

    private val template = ClassPathResource("template.xlsx").file
    private val filename = "fedresurs.xlsx"
    private val rowFlushSize = 100
    private val cellDatePattern = "d.m.yy h:mm"

    override suspend fun export(messages: Flow<Message>) {
        SXSSFWorkbook(XSSFWorkbook(template)).use { workbook ->
            Path(filename).outputStream().use { outputStream ->
                val sheet = workbook.getSheetAt(0)

                val dateFormat = workbook.creationHelper.createDataFormat().getFormat(cellDatePattern)
                val cellStyle = workbook.createCellStyle()
                cellStyle.dataFormat = dateFormat

                var rowNumber = 1
                messages.map { message ->
                    val row = sheet.createRow(++rowNumber)

                    var cellNumber = 0
                    var cell = row.createCell(cellNumber)
                    cell.setCellValue(message.guid)
                    cell = row.createCell(++cellNumber)
                    cell.setCellValue(message.number)
                    cell = row.createCell(++cellNumber)
                    cell.cellStyle = cellStyle
                    cell.setCellValue(message.datePublish)
                    cell = row.createCell(++cellNumber)
                    cell.cellStyle = cellStyle
                    cell.setCellValue(message.dateDisclosure)
                    cell = row.createCell(++cellNumber)
                    cell.setCellValue(message.type.description)
                    cell = row.createCell(++cellNumber)
                    cell.setCellValue(message.publisher.type.description)
                    cell = row.createCell(++cellNumber)
                    cell.setCellValue(message.publisher.data.fullName)
                    cell = row.createCell(++cellNumber)
                    cell.setCellValue(message.publisher.data.inn)
                    cell = row.createCell(++cellNumber)
                    cell.setCellValue(message.publisher.data.ogrn)
                    cell = row.createCell(++cellNumber)
                    cell.setCellValue(message.publisher.data.egrulAddress)
                    cell = row.createCell(++cellNumber)
                    cell.setCellValue(message.publisher.data.fio)
                    cell = row.createCell(++cellNumber)
                    cell.setCellValue(message.publisher.data.ogrnip)
                    cell = row.createCell(++cellNumber)
                    cell.setCellValue(message.publisher.data.name)
                    cell = row.createCell(++cellNumber)
                    cell.setCellValue(message.publisher.data.latinName)
                    cell = row.createCell(++cellNumber)
                    cell.setCellValue(message.publisher.data.countryCodeNum)
                    cell = row.createCell(++cellNumber)
                    cell.setCellValue(message.publisher.data.country)
                    cell = row.createCell(++cellNumber)
                    cell.setCellValue(message.publisher.data.regNum)
                    cell = row.createCell(++cellNumber)
                    cell.setCellValue(message.publisher.data.innOrAnalogue)
                    cell = row.createCell(++cellNumber)
                    cell.setCellValue(message.participants?.asCellValue { appendParticipant(it) })
                    cell = row.createCell(++cellNumber)
                    cell.setCellValue(message.content)
                    cell = row.createCell(++cellNumber)
                    cell.setCellValue(message.annulmentMessage?.asCellValue())
                    cell = row.createCell(++cellNumber)
                    cell.setCellValue(message.lockReason)
                    cell = row.createCell(++cellNumber)
                    cell.setCellValue(message.filesInfo.asCellValue { appendFileInfo(it) })
                    cell = row.createCell(++cellNumber)
                    cell.setCellValue(message.linkedMessages.asCellValue { appendLinkedMessage(it) })
                    cell = row.createCell(++cellNumber)
                    cell.setCellValue(message.notaryInfo?.asCellValue())
                    cell = row.createCell(++cellNumber)
                    cell.setCellValue(message.arbitrManagerInfo?.name)
                    cell = row.createCell(++cellNumber)
                    cell.setCellValue(message.contentAdditionalInfo?.asCellValue())
                }.onEach {
                    println("Message written")
                    if (rowNumber % rowFlushSize == 0) sheet.flushRows(rowFlushSize).also { println("Rows flushed") }
                }.onCompletion {
                    workbook.write(outputStream)
                    println("Export complete")
                }.collect()
            }
        }
    }

    private fun <T> List<T>.asCellValue(mapper: StringBuilder.(T) -> StringBuilder) = buildString {
        this@asCellValue.forEach { mapper(it).appendLine() }
    }

    private fun StringBuilder.appendParticipant(participant: Participant) =
        append(participant.role).append("/").appendLine(participant.type.description)
            .appendParticipantData(participant.data)

    private fun StringBuilder.appendParticipantData(data: ParticipantData) =
        append(data.fullName).append("/")
            .append(data.inn).append("/")
            .append(data.ogrn).append("/")
            .append(data.egrulAddress).append("/")
            .append(data.fio).append("/")
            .append(data.ogrnip).append("/")
            .append(data.name).append("/")
            .append(data.latinName).append("/")
            .append(data.countryCodeNum).append("/")
            .append(data.country).append("/")
            .append(data.regNum).append("/")
            .appendLine(data.innOrAnalogue)

    private fun AnnulmentMessage.asCellValue() = buildString {
        append(guid).append("/")
            .append(number).append("/")
            .append(datePublish).append("/")
            .append(type.name).append("/")
            .append(type.description)
    }

    private fun StringBuilder.appendFileInfo(fileInfo: FileInfo) =
        append(fileInfo.guid).append("/")
            .append(fileInfo.name).append("/")
            .append(fileInfo.size).append("/")
            .appendLine(fileInfo.isDangerous)

    private fun StringBuilder.appendLinkedMessage(linkedMessage: LinkedMessage) =
        append(linkedMessage.guid).append("/")
            .append(linkedMessage.number).append("/")
            .append(linkedMessage.type.name).append("/")
            .append(linkedMessage.type.description).append("/")
            .append(linkedMessage.datePublish).append("/")
            .append(linkedMessage.annulmentMessage?.guid).append("/")
            .append(linkedMessage.annulmentMessage?.number).append("/")
            .append(linkedMessage.annulmentMessage?.datePublish).append("/")
            .append(linkedMessage.annulmentMessage?.type?.name).append("/")
            .appendLine(linkedMessage.annulmentMessage?.type?.description)

    private fun NotaryInfo.asCellValue() = buildString {
        append(name).append("/").append(title)
    }

    private fun ContentAdditionalInfo.asCellValue() = buildString {
        append(companies?.asCellValue { appendParticipantData(it) })
            .append(message?.asCellValue())
    }
}