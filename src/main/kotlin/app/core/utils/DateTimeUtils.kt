package app.core.utils

import java.time.LocalDateTime
import java.time.ZoneId

fun getCurrentLocalDateTime(timeZone: String): LocalDateTime = LocalDateTime.now(ZoneId.of(timeZone))
fun getCurrentAlmatyLocalDateTime(): LocalDateTime = getCurrentLocalDateTime("Asia/Almaty")
