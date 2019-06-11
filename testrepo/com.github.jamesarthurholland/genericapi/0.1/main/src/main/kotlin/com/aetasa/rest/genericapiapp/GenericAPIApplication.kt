package com.aetasa.rest.genericapiapp

import com.aetasa.rest.genericapiapp.di.EmployeeModule
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import com.google.inject.Guice
import com.google.inject.Injector
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import java.util.concurrent.LinkedBlockingQueue

@EnableAutoConfiguration
@ComponentScan
class GenericAPIApplication {
	@Bean("queue")
	fun queue(): String = "yea this works"

	@Bean
	fun di(): Injector {
		val injector = Guice.createInjector(EmployeeModule())
		return injector
	}
}


fun main(args: Array<String>) {
	val injector = Guice.createInjector(EmployeeModule())

	runApplication<GenericAPIApplication>(*args)
}

