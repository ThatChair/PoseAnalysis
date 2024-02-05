package com.pose.analysis

import com.pose.analysis.App.Companion.logString
import com.pose.analysis.App.Companion.path
import com.pose.analysis.SliderPane.sliderPointPos
import javafx.scene.control.Label
import javafx.scene.input.MouseButton
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Line
import java.io.File
import java.time.Instant
import java.time.ZoneId

// Defines a custom println that logs the printed string to the logString
fun println(out: Any) {
    val text = out.toString()

    logString += text + "\n"

    kotlin.io.println(text)
}

// Takes the logString and writes it to a log file
fun writeLogFile() {
    val logFile = File("${path}\\logs\\log-${getTime()}.txt")

    if (!logFile.exists()) {
        logFile.createNewFile()
    }

    logFile.writeText(logString)
}

// Sets the color of a label
fun Label.setColor(color: Color): Label {

    // Changes the text fill to the provided color
    this.textFill = color

    // Returns the modified label
    return this
}

// Remaps a double from the specified range to the other specified range
fun Double.remap(fromRangeStart: Double, fromRangeEnd: Double, toRangeStart: Double, toRangeEnd: Double): Double {
    // Gets the range of the fromRange
    val fromRange = fromRangeEnd - fromRangeStart

    // Gets the range of the toRange
    val toRange = toRangeEnd - toRangeStart

    // Normalizes the value
    val normalizedValue = (this - fromRangeStart) / fromRange

    // Returns the range mapped to the new range
    return toRangeStart + normalizedValue * toRange
}

// Determines if the app is running from intellij or a JAR
val isRunningFromJar: Boolean
    get() {
    // Gets the class name
    val className = App::class.java.name.replace(".", "/") + ".class"

    // Gets the classpath
    val classPath = App::class.java.classLoader.getResource(className)?.toString() ?: return false

    // Returns whether the app is running from intellij or a JAR
    return classPath.startsWith("jar:")
}

// Calculates the dragging/playing of a slider
fun calculateSliderDrag(point: Circle, line: Line, minX: Double, maxX: Double) {
    // Triggers the calculation when the mouse is dragging the point on the slider
    point.setOnMouseDragged { event ->
        if (event.button == MouseButton.PRIMARY && !isAnimationPlaying.get()) {
            // Changes the position of the slider so that it is at the same position as the mouse, taking into account the minimum and maximum X values
            sliderPointPos.set(
                (sliderPointPos.get() + event.sceneX - point.centerX - point.radius).coerceIn(
                    minX,
                    maxX
                )
            )
        }
    }
    line.setOnMouseDragged { event ->
        if (event.button == MouseButton.PRIMARY && !isAnimationPlaying.get()) {
            // Changes the position of the slider so that it is at the same position as the mouse, taking into account the minimum and maximum X values
            sliderPointPos.set(
                (sliderPointPos.get() + event.sceneX - point.centerX - point.radius).coerceIn(
                    minX,
                    maxX
                )
            )
        }
    }
}

// Gets the current time in a nice format
fun getTime(): String {
    return Instant.now().atZone(ZoneId.systemDefault()).toString().replace(":", "-")
        .removeSuffix("[${ZoneId.systemDefault()}]").dropLast(16)
}
