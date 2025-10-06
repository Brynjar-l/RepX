package `is`.hi.hbv501g.repx

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RepXApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<RepXApplication>(*args)
        }
    }
}

