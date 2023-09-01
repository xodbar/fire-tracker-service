package app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FireTrackerServiceApplication

fun main(args: Array<String>) {
	runApplication<FireTrackerServiceApplication>(*args)
}
