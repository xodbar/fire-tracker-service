package app.web.controller

import app.core.utils.getCurrentAlmatyLocalDateTime
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/health")
class HealthController {

    @GetMapping
    @ResponseBody
    fun health() = getCurrentAlmatyLocalDateTime()
}
