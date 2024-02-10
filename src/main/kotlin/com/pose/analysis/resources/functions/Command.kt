package com.pose.analysis.resources.functions

import javafx.application.Platform
import java.io.BufferedReader
import java.io.InputStreamReader

// Runs a command
fun runCommand(commandString: String) {

    println("Running $commandString")

    val command = commandString.split(" ")

    // Creates and starts the process to run the python script with a command
    val processBuilder = ProcessBuilder(command)
    val process = processBuilder.start()

    // Capture and print standard error
    val errorReader = BufferedReader(InputStreamReader(process.inputStream))
    var errorLine: String?
    while (errorReader.readLine().also { errorLine = it } != null) {
        Platform.runLater {
            errorLine?.let { println(it) }
        }
    }

    // Waits for the process to finish
    val exitCode = process.waitFor()

    // Print the exit code
    println("Exit Code: $exitCode")

}

// Runs a command and gets the output
fun getCommand(commandString: String): String {

    println("Getting $commandString")

    val command = commandString.split(" ")

    // Creates and starts the process to run the python script with a command
    val processBuilder = ProcessBuilder(command)
    val process = processBuilder.start()

    // Capture and stores output
    val reader = BufferedReader(InputStreamReader(process.inputStream))
    var temp: String?
    var out = ""
    while (reader.readLine().also { temp = it } != null) {
        out += temp
    }

    // Waits for the process to finish
    val exitCode = process.waitFor()

    // Print the exit code
    println("Exit Code: $exitCode")

    // Returns output
    return out

}