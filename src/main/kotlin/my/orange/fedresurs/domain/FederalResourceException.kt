package my.orange.fedresurs.domain

class FederalResourceException(val code: Int, override val message: String) : RuntimeException(message)