package com.pose.analysis.resources.functions

import com.pose.analysis.App
import java.io.File

// Defines a custom println that logs the printed string to the logString
fun println(out: Any) {
    val text = out.toString()

    App.logString += text + "\n"

    kotlin.io.println(text)
}

// Takes the logString and writes it to a log file
fun writeLogFile() {
    val logFile = File("${App.path}\\logs\\log-${getTime()}.txt")

    if (!logFile.exists()) {
        logFile.createNewFile()
    }

    logFile.writeText(App.logString)
}