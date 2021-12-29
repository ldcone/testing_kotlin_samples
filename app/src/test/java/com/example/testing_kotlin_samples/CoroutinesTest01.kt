package com.example.testing_kotlin_samples

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.system.measureTimeMillis

class CoroutinesTest01 {

    @Test
    fun test01() = runBlocking{
        val time = measureTimeMillis {
            val name = getfirstName()
            val lastName = getLastName()
            print("Hello, $name $lastName\n")
        }
        print("meassure time : $time\n")

    }

    @Test
    fun test02() = runBlocking {
        val time = measureTimeMillis {
            val name  = async { getfirstName() }
            val lastName = async { getLastName() }
            print("Hello, ${name.await()} ${lastName.await()}\n")
        }
        print("meassure time : $time\n")
    }

    suspend fun getfirstName():String{
        delay(1000)
        return "이"
    }

    suspend fun getLastName():String{
        delay(1000)
        return "동천"
    }


}