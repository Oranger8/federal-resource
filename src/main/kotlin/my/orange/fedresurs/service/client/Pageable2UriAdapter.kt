package my.orange.fedresurs.service.client

import my.orange.fedresurs.domain.Pageable
import org.springframework.web.util.UriBuilder

class Pageable2UriAdapter : (UriBuilder, Pageable) -> UriBuilder {

    override fun invoke(builder: UriBuilder, pageable: Pageable) = with(pageable) {
        sort?.let { builder.queryParam("sort", it.value) }
        builder.queryParam("limit", limit)
            .queryParam("offset", offset)
    }
}