package com.pose.analysis.resources.extensions

import javafx.geometry.Point3D
import kotlin.math.cos
import kotlin.math.sin

// Reflects a Point3D along the specified axes
// Each axis has a boolean specifying whether to reflect along the axis
fun Point3D.reflect(x: Boolean, y: Boolean, z: Boolean): Point3D {
    return Point3D(
        if (x) -this.x else this.x,
        if (y) -this.y else this.y,
        if (z) -this.z else this.z
    )
}

// Converts a Point3D to the screenspace using the camera position and the focal length. (It is assumed that the camera is pointed towards the screen center and the screen is perpendicular to the camera)
fun Point3D.toScreenSpace(focalLength: Double, cameraPosition: Point3D): Point3D {

    // Finds the focal point, determined by adding a vector perpendicular to the camera and with a magnitude of the focal length to the camera position
    val focalPoint = cameraPosition.scale(this.magnitude() - focalLength)

    // Return a point 3d with only x and y coordinates (z is zero, never used)
    return Point3D(
        (this.x * focalLength) / (this.z - focalLength) + focalPoint.x,
        (this.y * focalLength) / (this.z - focalLength) + focalPoint.y,
        0.0
    )
}

// Rotates a Point3D around the origin
fun Point3D.rotate(yaw: Double, pitch: Double): Point3D {

    // Some resources before so it doesn't happen multiple times
    val a = cos(pitch)
    val b = sin(pitch)
    val c = cos(yaw)
    val d = sin(yaw)

    /*
    returns the point, rotated with rotation matrices.
    equal to [xRotMatrix][yRotMatrix][pointMatrix]
     */
    return Point3D(
        (c * this.x) + (d * this.z),
        (b * d * this.x) + (a * this.y) - (c * b * this.z),
        -(a * d * this.x) + (b * this.y) + (a * c * this.z)
    )
}

// Scales the Point3D towards the origin to have the given magnitude
fun Point3D.scale(newMagnitude: Double): Point3D {
    // Gets the current magnitude of the point
    val mag = this.magnitude()

    // Returns the point scaled to the given magnitude
    return this.multiply(newMagnitude / mag)
}