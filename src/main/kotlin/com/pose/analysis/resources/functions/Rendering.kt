package com.pose.analysis.resources.functions

import com.pose.analysis.App
import com.pose.analysis.Pane3D
import com.pose.analysis.resources.classes.Connections
import com.pose.analysis.resources.classes.Wireframe3D
import com.pose.analysis.resources.extensions.remap
import javafx.geometry.Point3D
import javafx.scene.layout.Pane
import javafx.scene.shape.Circle
import javafx.scene.shape.Line

// Big render function!
fun Pane.render(
    animation: Array<Wireframe3D>,
    connections: Connections,
    yaw: Double,
    pitch: Double,
    frameNum: Int,
    camPos: Point3D,
    focalLength: Double,
    scale: Double
) {
    // Clears everything from the pane to start fresh
    this.children.clear()

    // Sets the renderList, which is a wireframe that has been centered at the origin and rotate
    var renderList = animation[
        if (frameNum >= animation.size) animation.size - 1 else frameNum
    ].reflect(x = true, y = false, z = false).centerPointsAtOrigin().rotate(yaw, pitch)

    // Sets the minimum and maximum dot sizes
    // What did you think this did
    val minDotSize = 5.0
    val maxDotSize = 7.5

    // Makes a list of the inverse of the distances from the dots to the camera
    val dotSizes = renderList.points.map {
        1 / it.distance(camPos)
    }

    // Finds the max and minimum dot sizes
    val maxDist = dotSizes.max()
    val minDist = dotSizes.min()

    // Smashes the 3d renderList to 2d for the screen
    renderList = renderList.toScreenSpace(focalLength, Pane3D.cameraPos)

    // Loops over every point in the to be rendered wireframe
    for (i in renderList.points.indices) {

        // Creates the dot at each point with the correct size fill, etc.
        val dot = Circle()
        dot.fill = App.textColor
        dot.radius = dotSizes[i].remap(minDist, maxDist, minDotSize, maxDotSize) * scale * 0.005
        dot.centerX = renderList.points[i].x * scale
        dot.centerY = renderList.points[i].y * scale

        // Add the dot to the pane
        this.children.add(
            dot
        )

        // Creates the connections between the dots
        for (j in connections.getConnections(i).indices) {

            // Initializes the line
            val line = Line()

            // Sets the correct color and width
            line.stroke = App.textColor
            line.strokeWidth = 2.0

            // Calculates the x and y coordinates of the points the line connects using the numbers in the connection list
            line.startX = renderList.points[i].x * scale
            line.startY = renderList.points[i].y * scale
            line.endX = renderList.points[connections.getConnections(i)[j] - 11].x * scale
            line.endY = renderList.points[connections.getConnections(i)[j] - 11].y * scale

            // Adds the line to the pane
            this.children.add(
                line
            )
        }
    }
}