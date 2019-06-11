package com.aetasa.rest.genericapiapp.di

import com.aetasa.rest.genericapiapp.employee.Employee
import com.aetasa.rest.genericapiapp.generic.GenericService
import com.google.inject.AbstractModule
import com.google.inject.name.Names
import kotlin.jvm.javaClass

class EmployeeModule : AbstractModule(){
    override fun configure() {
//        bind(GenericService::class.java).annotatedWith(Names.named("EmployeeService"))


//        bind(javaClass<GenericService>())!!.annotateWith(Names.named("EmployeeService"))

        bind(GenericService::class.java)
                .annotatedWith(Names.named("EmployeeService"))
                .toInstance(GenericService<Employee>(Employee::class.java))
    }
}
