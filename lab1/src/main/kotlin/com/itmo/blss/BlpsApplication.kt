package com.itmo.blss

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableProcessApplication
class BlssApplication

fun main(args: Array<String>) {
	runApplication<BlssApplication>(*args)
}
