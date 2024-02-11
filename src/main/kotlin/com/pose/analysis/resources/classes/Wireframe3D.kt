package com.pose.analysis.resources.classes

import com.pose.analysis.resources.extensions.reflect
import com.pose.analysis.resources.extensions.rotate
import com.pose.analysis.resources.extensions.toScreenSpace
import javafx.geometry.Point3D

// A wireframe, which is really just an array of points
class Wireframe3D(var points: Array<Point3D>) {

    // Reflects all points across the specified axes
    fun reflect(x: Boolean, y: Boolean, z: Boolean): Wireframe3D {
        return Wireframe3D((this.points.map { it.reflect(x, y, z) }).toTypedArray())
    }

    // Converts all points to the screen. See the same function in Point3D.kt
    fun toScreenSpace(focalLength: Double, cameraPos: Point3D): Wireframe3D {
        return Wireframe3D((this.points.map { it.toScreenSpace(focalLength, cameraPos) }).toTypedArray())
    }

    // Rotates all points about the origin. See the same function in Point3D.kt
    fun rotate(yaw: Double, pitch: Double): Wireframe3D {
        return Wireframe3D((this.points.map { it.rotate(yaw, pitch) }).toTypedArray())
    }

    // Centers the points about the origin
    fun centerPointsAtOrigin(): Wireframe3D {
        // Calculate the mean for each dimension (x, y, z)
        val meanX = this.points.map { it.x }.average()
        val meanY = this.points.map { it.y }.average()
        val meanZ = this.points.map { it.z }.average()

        // Subtract the mean values from each dimension of every point
        return Wireframe3D(this.points.map { point ->
            Point3D(point.x - meanX, point.y - meanY, point.z - meanZ)
        }.toTypedArray())
    }
}