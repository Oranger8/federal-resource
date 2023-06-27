package my.orange.fedresurs.domain

import com.fasterxml.jackson.annotation.JsonValue

data class Pageable(
    val sort: Sort?,
    val limit: Int,
    val offset: Int
)

enum class Sort(@JsonValue val value: String, val description: String) {
    
    ASC("DATE:asc", "в хронологическом порядке"),
    DESC("DATE:desc", "в порядке обратном хронологическому")
}
