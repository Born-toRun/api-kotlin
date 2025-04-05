package kr.kro.btr

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ApiKotlinApplication

fun main(args: Array<String>) {
    runApplication<ApiKotlinApplication>(*args)
}
