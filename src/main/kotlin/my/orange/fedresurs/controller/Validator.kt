package my.orange.fedresurs.controller

interface Validator<in T> {
    
    fun validate(value: T)
}