package com.pose.analysis

import com.pose.analysis.App.Companion.textColor
import com.pose.analysis.MainPane.middlePane
import javafx.beans.binding.Bindings
import javafx.scene.layout.Pane
import javafx.scene.shape.Circle
import javafx.scene.shape.Line
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.ndarray
import org.jetbrains.kotlinx.multik.ndarray.data.get
import kotlin.math.pow
import kotlin.math.sqrt

object Pane3D : Pane() {

    // Standard mediapipe without the head...
    // This sets up the lines connecting the dots of the 3D render.
    // Each array is the connections for the dot at that index (i.e. index on connects to index 12, 13, & 23)
    // The first couple are commented out because the head is not rendered
    private val personConnections = arrayOf(
//        arrayOf(2, 5),
//        arrayOf(),
//        arrayOf(7),
//        arrayOf(),
//        arrayOf(),
//        arrayOf(8),
//        arrayOf(),
//        arrayOf(),
//        arrayOf(),
//        arrayOf(10),
//        arrayOf(),
        arrayOf(12, 13, 23),
        arrayOf(14, 24),
        arrayOf(15),
        arrayOf(16),
        arrayOf(17, 19, 21),
        arrayOf(18, 20, 22),
        arrayOf(),
        arrayOf(),
        arrayOf(),
        arrayOf(),
        arrayOf(),
        arrayOf(),
        arrayOf(24, 25),
        arrayOf(26),
        arrayOf(27),
        arrayOf(28),
        arrayOf(29, 31),
        arrayOf(30, 32),
        arrayOf(31),
        arrayOf(32),
        arrayOf(),
        arrayOf()
    )

    // Specifies the camera position and the screen position in 3d space
    private val cameraPos = mk.ndarray(mk[0.0, 0.0, 5.0])
    private val screenPos = mk.ndarray(mk[0.0, 0.0, 2.0])

    // Stores the x and y angle the person is rotated at
    private var yAngle = 0.0
    private var xAngle = 0.0

    // Stores the zoom level
    var zoom = 200.0


    // Stores the previous mouse x and y coordinates so the x and y angles are incremented
    private var lastX = 0.0
    private var lastY = 0.0

    init {

        // Specifies the position of the Pane3D
        Pane3D.layoutX = middlePane.prefWidth / 2.0
        Pane3D.layoutY = middlePane.prefHeight / 2.0


        // Makes sure the Pane3D is only visible when the welcome message is gone and the app isn't loading
        Pane3D.visibleProperty().bind(
            Bindings.`when`(
                isWelcome.not()
                    .and(MainPane.loadingGif.visibleProperty().not())
            )
                .then(true)
                .otherwise(false)
        )

        // Set up the mouse drag event handler
        Pane3D.setOnMousePressed { event ->
            lastX = event.sceneX
            lastY = event.sceneY
        }

        // Calculates how much to change the angleX and angleY when the mouse is dragged
        Pane3D.setOnMouseDragged { event ->

            // Gets the change in mouse position
            val deltaX = event.sceneX - lastX
            val deltaY = event.sceneY - lastY

            // Uses the change in mouse position to change the angleX and angleY
            yAngle += deltaX * Math.PI / 180
            xAngle += deltaY * Math.PI / 180

            // Re-renders the person
            render(currentFrame, zoom)

            // Update lastX and lastY for the next frame
            lastX = event.sceneX
            lastY = event.sceneY
        }

        // Handles zooming
        Pane3D.setOnZoom { event ->
            zoom *= event.zoomFactor
            render(currentFrame, zoom)
        }

        // Also handles zooming
        Pane3D.setOnScroll { event ->
            zoom += event.deltaX
            render(currentFrame, zoom)
        }
    }

    // Big render function. too lazy to comment rn
    fun render(frame: Int, scale: Double) {
        Pane3D.children.clear()
        var renderList = animation[
            if (frame >= animation.size) animation.size - 1 else frame
        ].reflectX()
            .centerPointsAtOrigin().rotateY(yAngle).rotateX(xAngle)

        val minDotSize = 5.0
        val maxDotSize = 7.5

        val dotSizes = renderList.map {
            1 / sqrt(
                (it[0] - cameraPos[0]).pow(2) + (it[1] - cameraPos[1]).pow(2) + (it[2] - cameraPos[2]).pow(2)
            )
        }

        val maxDist = dotSizes.max()
        val minDist = dotSizes.min()

        renderList = renderList.toScreenSpace(screenPos, cameraPos)

        for (i in renderList.indices) {
            val dot = Circle()
            dot.fill = textColor
            dot.radius = dotSizes[i].remap(minDist, maxDist, minDotSize, maxDotSize)
            dot.centerX = renderList[i][0] * scale
            dot.centerY = renderList[i][1] * scale
            Pane3D.children.add(
                dot
            )

            for (j in personConnections[i].indices) {
                val line = Line()
                line.stroke = textColor
                line.strokeWidth = 2.0
                line.startX = renderList[i][0] * scale
                line.startY = renderList[i][1] * scale
                line.endX = renderList[personConnections[i][j] - 11][0] * scale
                line.endY = renderList[personConnections[i][j] - 11][1] * scale
                Pane3D.children.add(
                    line
                )
            }
        }
    }
}