package com.aetasa.rest.genericapiapp.generic;

import com.google.inject.Inject
import com.google.inject.Injector
import com.google.inject.Key
import com.google.inject.name.Names
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

import com.github.salomonbrys.kotson.*
import com.google.gson.Gson

@RestController
class GenericController {
    @Inject
    lateinit var service: GenericService<*>

//    @Autowired // Let Spring inject the queue
    lateinit var queue: String

    @Autowired
    lateinit var di: Injector

    @Autowired
    fun getIt(@Qualifier("queue") p: String) {
        this.queue = p
    }

    fun fetch() = "Hello SpringBoot" + queue

    @RequestMapping(value = ["/generic/{serviceName:[a-z-]+}"])
    @ResponseBody
    fun helloSpringBoot(@PathVariable serviceName: String): String {
        var service = di.getInstance(Key.get(GenericService::class.java, Names.named("EmployeeService")))

        val employee = service.fetch(1)
        val gson = Gson()
        return gson.toJson(employee)
    }
}
