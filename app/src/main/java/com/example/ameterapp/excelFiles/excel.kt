package com.example.ameterapp.excelFiles

import java.io.BufferedReader
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.util.*

private val CSV_HEADER = "id,name,address,age"

private val CUSTOMER_ID_IDX = 0
private val CUSTOMER_NAME_IDX = 1
private val CUSTOMER_ADDRESS_IDX = 2
private val CUSTOMER_AGE = 3

fun main() {
    writeExcelFile()
    readExcelFile()
}

fun readExcelFile() {
    var fileReader: BufferedReader? = null

    try {
        val customers = ArrayList<Customer>()
        var line: String?

        fileReader = BufferedReader(FileReader("customer.csv"))

        // Read CSV header
        fileReader.readLine()

        // Read the file line by line starting from the second line
        line = fileReader.readLine()
        while (line != null) {
            val tokens = line.split(",")
            if (tokens.size > 0) {
                val customer = Customer(
                        tokens[CUSTOMER_ID_IDX],
                        tokens[CUSTOMER_NAME_IDX],
                        tokens[CUSTOMER_ADDRESS_IDX],
                        Integer.parseInt(tokens[CUSTOMER_AGE]))
                customers.add(customer)
            }

            line = fileReader.readLine()
        }

        // Print the new customer list
        for (customer in customers) {
            println(customer)
        }
    } catch (e: Exception) {
        println("Reading CSV Error!")
        e.printStackTrace()
    } finally {
        try {
            fileReader!!.close()
        } catch (e: IOException) {
            println("Closing fileReader Error!")
            e.printStackTrace()
        }
    }
}

fun writeExcelFile() {
    val customers = Arrays.asList(
            Customer("1", "Jack Smith", "Massachusetts", 23),
            Customer("2", "Adam Johnson", "New York", 27),
            Customer("3", "Katherin Carter", "Washington DC", 26),
            Customer("4", "Jack London", "Nevada", 33),
            Customer("5", "Jason Bourne", "California", 36))

    var fileWriter: FileWriter? = null

    try {
        fileWriter = FileWriter("customer.csv")
        fileWriter.append(CSV_HEADER)
        fileWriter.append('\n')

        for (customer in customers) {
            fileWriter.append(customer.id)
            fileWriter.append(',')
            fileWriter.append(customer.name)
            fileWriter.append(',')
            fileWriter.append(customer.address)
            fileWriter.append(',')
            fileWriter.append(customer.age.toString())
            fileWriter.append('\n')
        }

        println("Write CSV successfully!")

    } catch (e: Exception) {
        println("Writing CSV error!")
        e.printStackTrace()
    } finally {
        try {
            fileWriter!!.flush()
            fileWriter.close()
        } catch (e: IOException) {
            println("Flushing/closing error!")
            e.printStackTrace()
        }
    }

}
