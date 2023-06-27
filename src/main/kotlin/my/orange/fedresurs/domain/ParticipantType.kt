package my.orange.fedresurs.domain

import com.fasterxml.jackson.annotation.JsonValue

enum class ParticipantType(@JsonValue val value: String, val description: String) {

    COMPANY("Company", "ЮЛ"),
    INDIVIDUAL_ENTREPRENEUR("IndividualEntrepreneur", "ИП"),
    PERSON("Person", "ФЛ"),
    APPRAISER("Appraiser", "Оценщик"),
    NON_RESIDENT_COMPANY("NonResidentCompany", "Иностранная компания"),
    FOREIGN_SYSTEM("ForeignSystem", "Внешняя система")
}