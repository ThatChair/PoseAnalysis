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

object `3DPane` : Pane() {

    // Standard mediapipe without the head...
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

    private val cameraPos = mk.ndarray(mk[0.0, 0.0, 5.0])
    private val screenPos = mk.ndarray(mk[0.0, 0.0, 2.0])
    private var yAngle = 0.0
    private var xAngle = 0.0
    var zoom = 200.0

    private var lastX = 0.0
    private var lastY = 0.0

    init {
//        println(middlePane.width / 2.0)
        `3DPane`.layoutX = middlePane.prefWidth / 2.0
        `3DPane`.layoutY = middlePane.prefHeight / 2.0


        `3DPane`.visibleProperty().bind(
            Bindings.`when`(
                isWelcome.not()
                    .and(MainPane.loadingGif.visibleProperty().not())
            )
                .then(true)
                .otherwise(false)
        )

        // Set up the mouse drag event handler
        `3DPane`.setOnMousePressed { event ->
            lastX = event.sceneX
            lastY = event.sceneY
        }

        `3DPane`.setOnMouseDragged { event ->
            val deltaX = event.sceneX - lastX
            val deltaY = event.sceneY - lastY

            yAngle += deltaX * Math.PI / 180
            xAngle += deltaY * Math.PI / 180
            render(currentFrame, zoom)

            // Update lastX and lastY for the next frame
            lastX = event.sceneX
            lastY = event.sceneY
        }

        `3DPane`.setOnZoom { event ->
            zoom *= event.zoomFactor
            render(currentFrame, zoom)
        }

        `3DPane`.setOnScroll { event ->
            zoom += event.deltaX
            render(currentFrame, zoom)
        }
    }

    fun render(frame: Int, scale: Double) {
        `3DPane`.children.clear()
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
            `3DPane`.children.add(
                dot
            )

            for (j in personConnections[i].indices) {
                val line = Line()
                line.stroke = textColor
                line.strokeWidth = 2.0
                line.startX = renderList[i][0] * scale
                line.startY = renderList[i][1] * scale
                println("I: $i")
                println("J: $j")
                println("renderListSize: ${renderList.size}")
                println("personConnectionsSize: ${personConnections.size}")
                line.endX = renderList[personConnections[i][j] - 11][0] * scale
                line.endY = renderList[personConnections[i][j] - 11][1] * scale
                `3DPane`.children.add(
                    line
                )
            }


//            println("Rendering Dot at X: ${dot.centerX}, Y: ${dot.centerY}")


        }
//        println(`3DPane`.children)
    }


}
