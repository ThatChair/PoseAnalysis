package com.pose.analysis.resources.functions

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.pose.analysis.SliderPane
import com.pose.analysis.isAnimationPlaying
import com.pose.analysis.resources.classes.Wireframe3D
import javafx.scene.input.MouseButton
import javafx.scene.shape.Circle
import javafx.scene.shape.Line
import java.net.URL
import java.time.Instant
import java.time.ZoneId

// Calculates the dragging/playing of a slider
fun calculateSliderDrag(point: Circle, line: Line, minX: Double, maxX: Double) {
    // Triggers the calculation when the mouse is dragging the point on the slider
    point.setOnMouseDragged { event ->
        if (event.button == MouseButton.PRIMARY && !isAnimationPlaying.get()) {
            // Changes the position of the slider so that it is at the same position as the mouse, taking into account the minimum and maximum X values
            SliderPane.sliderPointPos.set(
                (SliderPane.sliderPointPos.get() + event.sceneX - point.centerX - point.radius).coerceIn(
                    minX,
                    maxX
                )
            )
        }
    }
    line.setOnMouseDragged { event ->
        if (event.button == MouseButton.PRIMARY && !isAnimationPlaying.get()) {
            // Changes the position of the slider so that it is at the same position as the mouse, taking into account the minimum and maximum X values
            SliderPane.sliderPointPos.set(
                (SliderPane.sliderPointPos.get() + event.sceneX - point.centerX - point.radius).coerceIn(
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

// Gets the latest release of the app in integer form
fun getLatestRelease(): Int {
    try {
        val url = URL("https://api.github.com/repos/thatchair/poseanalysis/tags")
        val jsonString = url.readText()
        return Gson().fromJson(jsonString, JsonArray::class.java)[0].asJsonObject["name"].asString.drop(1)
            .replace(".", "").toInt()
    } catch (_: Exception) {
        return 0
    }
}

// Returns angular velocity of a specified arm in radians
fun Array<Wireframe3D>.getAngularVelocity(frame: Int, isLeftArm: Boolean = false): Double {
    if (frame >= this.size || frame == 0) return 0.0
    val shoulderID = if (isLeftArm) 11 else 12
    val fingerID = if (isLeftArm) 19 else 20

    val prevShoulderPos = this[frame - 1].points[shoulderID]
    val shoulderPos = this[frame].points[shoulderID]

    val prevFingerPos = this[frame - 1].points[fingerID]
    val fingerPos = this[frame].points[fingerID]

    val prevAngle = prevShoulderPos.angle(prevFingerPos)
    val currentAngle = shoulderPos.angle(fingerPos)

    return currentAngle - prevAngle
}