package io.alron.planner.presentation.util

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

const val WORKING_START_HOUR = 8
const val WORKING_FINISH_HOUR = 21

fun LocalDate.toRuFormat(): String = this.format(
    DateTimeFormatter.ofPattern(
        "dd MMMM yyyy",
        Locale.forLanguageTag("ru-RU")
    )
)

fun Long.toRuFormat(): String {
    val formatter = DateTimeFormatter.ofPattern(
        "dd.MM.yyyy HH:mm",
        Locale.forLanguageTag("ru-RU")
    )
        .withZone(ZoneId.systemDefault())
    return formatter.format(Instant.ofEpochMilli(this))
}