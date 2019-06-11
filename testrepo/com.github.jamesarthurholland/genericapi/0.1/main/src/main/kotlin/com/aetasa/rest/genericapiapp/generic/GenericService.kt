package com.aetasa.rest.genericapiapp.generic

import com.aetasa.rest.genericapiapp.employee.Employee

import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.util.*
import javax.persistence.EntityManagerFactory
import javax.persistence.Persistence


class GenericService<T>(val classLiteral: Class<T>) {
    fun fetch(id: Int): T{
        var emfInstance: EntityManagerFactory? = null
        try {
            val envProperties = Properties()
//            envProperties.load(ApplicationProperties::class.java!!.getResourceAsStream("/env.properties")) TODO
//            emfInstance = Persistence.createEntityManagerFactory("hdf-mysql", envProperties)

            envProperties.load(GenericService::class.java!!.getResourceAsStream("/application.properties"))
            emfInstance = Persistence.createEntityManagerFactory("genericapi-mysql", envProperties)
        } catch (ex: Throwable) {
            // Log exception!
            throw ExceptionInInitializerError(ex)
        }

        var emManager = emfInstance.createEntityManager()

        var employee = emManager.find(classLiteral, 1)

        return employee
//        val employee = Employee(1, "a", "b", "c", "d")

//        val factory: SessionFactory
//        try {
//            factory = Configuration().configure().buildSessionFactory()
//        } catch (ex: Throwable) {
//            System.err.println("Failed to create sessionFactory object.$ex")
//            throw ExceptionInInitializerError(ex)
//        }
//
//
//        val session = factory.openSession()
//        var tx: Transaction? = null
//
//        try {
//            tx = session.beginTransaction();
//            val employee = session.get(Employee::class.java, id)
//            tx.commit()
//        } catch (e: HibernateException) {
//            if (tx!=null) tx.rollback()
//            e.printStackTrace()
//        } finally {
//            session.close()
//        }
    }
}
