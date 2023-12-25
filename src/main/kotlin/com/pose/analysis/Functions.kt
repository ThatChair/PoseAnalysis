package com.pose.analysis

import com.pose.analysis.SliderPane.sliderPointPos
import javafx.scene.control.Label
import javafx.scene.input.MouseButton
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Line

// Sets the color of a label
fun Label.setColor(color: Color): Label {

    // Changes the text fill to the provided color
    this.textFill = color

    // Returns the modified label
    return this
}

// Determines if the app is running from intellij or a JAR
fun isRunningFromJar(): Boolean {
    // Gets the class name
    val className = App::class.java.name.replace(".", "/") + ".class"

    // Gets the classpath
    val classPath = App::class.java.classLoader.getResource(className)?.toString() ?: return false

    // Returns whether the app is running from intellij or a JAR
    return classPath.startsWith("jar:")
}

// Calculates the dragging/playing of a slider
fun calculateSliderDrag(point: Circle, line: Line, minX: Double, maxX: Double): Circle {
    // Triggers the calculation when the mouse is dragging the point on the slider
    point.setOnMouseDragged { event ->
        if (event.button == MouseButton.PRIMARY) {
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
        if (event.button == MouseButton.PRIMARY) {
            // Changes the position of the slider so that it is at the same position as the mouse, taking into account the minimum and maximum X values
            sliderPointPos.set(
                (sliderPointPos.get() + event.sceneX - point.centerX - point.radius).coerceIn(
                    minX,
                    maxX
                )
            )
        }
    }
    // Returns the translated point
    return point
}
