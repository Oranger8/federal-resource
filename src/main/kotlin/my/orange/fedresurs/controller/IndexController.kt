package my.orange.fedresurs.controller

import my.orange.fedresurs.domain.MessageDesc
import my.orange.fedresurs.domain.MessageFilter
import my.orange.fedresurs.service.ExcelService
import my.orange.fedresurs.service.FederalResourceService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.result.view.Rendering
import java.time.LocalDateTime

@RestController
class IndexController(
    private val service: FederalResourceService,
    private val excelService: ExcelService
) {
    
    @GetMapping("/")
    suspend fun getMessages(): Rendering {
        val start = LocalDateTime.of(2020, 1, 1, 0, 0)
        val filter = MessageFilter(
            dateBegin = start,
            dateEnd = start.plusDays(30)
        )
//        val pageable = Pageable(null, 10, 0)
//        val messages = service.getMessages(filter, pageable)?.messages.orEmpty()
        return Rendering.view("index")
            .modelAttribute("messages", emptyList<MessageDesc>())
            .modelAttribute("filter", filter)
            .build()
    }
    
    @PostMapping("/export")
    suspend fun export() {
        val filter = MessageFilter(
            dateBegin = LocalDateTime.of(2023, 1, 1, 0, 0, 0),
            dateEnd = LocalDateTime.now()
        )
        val messageFlow = service.export(filter)
        excelService.export(messageFlow)
    }
}