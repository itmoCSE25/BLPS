package com.itmo.blss

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BlssApplication

fun main(args: Array<String>) {
	runApplication<BlssApplication>(*args)
}
