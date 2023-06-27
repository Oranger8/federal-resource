package my.orange.fedresurs.controller

import my.orange.fedresurs.domain.Pageable
import org.springframework.stereotype.Component

@Component
class PageableValidator : Validator<Pageable> {
    
    private val limitRange = (1..500)
    
    override fun validate(value: Pageable) {
        with(value) {
            require(limit in limitRange)
            require(offset >= 0)
        }
    }
}