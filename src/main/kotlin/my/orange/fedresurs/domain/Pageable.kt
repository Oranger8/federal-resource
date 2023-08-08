package my.orange.fedresurs.domain

import com.fasterxml.jackson.annotation.JsonValue

data class Pageable(
    val sort: Sort?,
    val limit: Int,
    val offset: Int
) {
    
    fun hasNext(total: Int) = total > offset + limit
    
    fun next() = Pageable(sort, limit, offset + limit)
}

enum class Sort(@JsonValue val value: String, val description: String) {
    
    ASC("DATE:asc", "в хронологическом порядке"),
    DESC("DATE:desc", "в порядке обратном хронологическому")
}
