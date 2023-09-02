package app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class FireTrackerServiceApplication

fun main(args: Array<String>) {
    runApplication<FireTrackerServiceApplication>(*args)
}
