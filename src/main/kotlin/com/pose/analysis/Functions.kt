package com.pose.analysis

import com.pose.analysis.SliderPane.sliderPointPos
import javafx.scene.control.Label
import javafx.scene.input.MouseButton
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Line
import org.jetbrains.kotlinx.multik.api.linalg.dot
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.ndarray
import org.jetbrains.kotlinx.multik.ndarray.data.D1
import org.jetbrains.kotlinx.multik.ndarray.data.D1Array
import org.jetbrains.kotlinx.multik.ndarray.data.NDArray
import org.jetbrains.kotlinx.multik.ndarray.data.get
import kotlin.math.cos
import kotlin.math.sin

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
fun isRunningFromJar(): Boolean {
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

fun Array<D1Array<Double>>.reflectX(): Array<D1Array<Double>> {
    return (this.map { it.reflectX() }).toTypedArray()
}

fun D1Array<Double>.reflectX(): D1Array<Double> {
    return mk.ndarray(mk[-this[0], this[1], this[2]])
}

// Assumed that the screen is perpendicular to the camera position & camera is facing origin
fun Array<D1Array<Double>>.toScreenSpace(
    screenCenterPos: D1Array<Double>,
    cameraPos: D1Array<Double>
): List<D1Array<Double>> {
    return this.map { it.toScreenSpace(screenCenterPos, cameraPos) }
}

fun List<NDArray<Double, D1>>.toScreenSpace(
    screenCenterPos: D1Array<Double>,
    cameraPos: D1Array<Double>
): List<D1Array<Double>> {
    return this.map { it.toScreenSpace(screenCenterPos, cameraPos) }
}

fun D1Array<Double>.toScreenSpace(screenCenter: D1Array<Double>, cameraPosition: D1Array<Double>): D1Array<Double> {
    require(this.size == 3) { "Input array must have three coordinates" }
    require(cameraPosition.size == 3) { "Camera position array must have three coordinates" }
    require(screenCenter.size == 3) { "Screen center array must have three coordinates" }

    val focalLength = cameraPosition[2] - screenCenter[2]

    // Adjust the calculation to use screenCenter as the origin
    val x = (this[0] * focalLength) / (this[2] - focalLength) + screenCenter[0]
    val y = (this[1] * focalLength) / (this[2] - focalLength) + screenCenter[1]

    // Return a 1D array with x and y coordinates
    return mk.ndarray(mk[x, y])
}

fun Array<D1Array<Double>>.rotateY(radians: Double): List<D1Array<Double>> {
    return this.map { it.rotateY(radians) }
}

fun D1Array<Double>.rotateY(radians: Double): D1Array<Double> {
    // Create the 3x3 rotation matrix for rotation around the y-axis
    val rotationMatrix = mk.ndarray(
        mk[
            mk[cos(radians), 0.0, sin(radians)],
            mk[0.0, 1.0, 0.0],
            mk[-sin(radians), 0.0, cos(radians)]
        ]
    )

    // Ensure the point and rotation matrix dimensions match
    require(this.size == 3 && rotationMatrix.size == 9) { "Invalid dimensions for point or rotation matrix" }

    val point = mk.ndarray(
        mk[
            mk[this[0]],
            mk[this[1]],
            mk[this[2]]
        ]
    )

    // Perform the dot product
    val result = rotationMatrix.dot(point)

    // Extract the elements from the result and create a 1D array
    return mk.ndarray(mk[result[0][0], result[1][0], result[2][0]])
}


fun Array<D1Array<Double>>.rotateX(radians: Double): List<D1Array<Double>> {
    return this.map { it.rotateX(radians) }
}

fun List<NDArray<Double, D1>>.rotateX(radians: Double): List<D1Array<Double>> {
    return this.map { it.rotateX(radians) }
}


fun D1Array<Double>.rotateX(radians: Double): D1Array<Double> {
    // Create the 3x3 rotation matrix for rotation around the y-axis
    val rotationMatrix = mk.ndarray(
        mk[
            mk[1.0, 0.0, 0.0],
            mk[0.0, cos(radians), -sin(radians)],
            mk[0.0, sin(radians), cos(radians)]
        ]
    )

    // Ensure the point and rotation matrix dimensions match
    require(this.size == 3 && rotationMatrix.size == 9) { "Invalid dimensions for point or rotation matrix" }

    val point = mk.ndarray(
        mk[
            mk[this[0]],
            mk[this[1]],
            mk[this[2]]
        ]
    )

    // Perform the dot product
    val result = rotationMatrix.dot(point)

    // Extract the elements from the result and create a 1D array
    return mk.ndarray(mk[result[0][0], result[1][0], result[2][0]])
}

fun Array<D1Array<Double>>.centerPointsAtOrigin(): Array<D1Array<Double>> {
    // Calculate the mean for each dimension (x, y, z)
    val meanX = this.map { it[0] }.average()
    val meanY = this.map { it[1] }.average()
    val meanZ = this.map { it[2] }.average()

    // Subtract the mean values from each dimension of every point
    return this.map { point ->
        val centeredX = point[0] - meanX
        val centeredY = point[1] - meanY
        val centeredZ = point[2] - meanZ
        mk.ndarray(mk[centeredX, centeredY, centeredZ])
    }.toTypedArray()
}
